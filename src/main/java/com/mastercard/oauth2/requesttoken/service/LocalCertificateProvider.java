package com.mastercard.oauth2.requesttoken.service;

import com.mastercard.oauth2.requesttoken.constants.JWTSupportedAlgorithm;
import com.mastercard.oauth2.requesttoken.constants.Oauth2Constants;
import com.mastercard.oauth2.requesttoken.exception.KeyRetrievalException;
import com.mastercard.oauth2.requesttoken.models.SignerPublicKey;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class LocalCertificateProvider {

    private static final Logger log = LoggerFactory.getLogger(com.mastercard.oauth2.requesttoken.service.LocalCertificateProvider.class);

    private final String keystoreLocation;

    private final String alias;

    private final String password;

    private final String keyStoreType;

    public LocalCertificateProvider(
            String keystoreLocation,
            String alias,
            String password,
            String keyStoreType) {

        this.keystoreLocation = keystoreLocation;
        this.alias = alias;
        this.password = password;
        this.keyStoreType = keyStoreType;

         if (keystoreLocation == null) {
            final String message = "The provided keystore location was null";
            log.error(message);
            throw new IllegalArgumentException(message);
        }

        if (password == null) {
            final String message =
                    "Password provided for keystore " + keystoreLocation + " is null.";
            log.error(message);
            throw new KeyRetrievalException(message);
        }

        if (alias == null) {
            final String message = "alias provided is null.";
            log.error(message);
            throw new KeyRetrievalException(message);
        }
    }

    private PrivateKey getPrivateKeyByAlias(String alias) {
        try {
            KeyStore keyStore = loadKeyStore(keystoreLocation, password, keyStoreType);
            return (PrivateKey) keyStore.getKey(alias, password.toCharArray());
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            log.error("Exception while attempting to get private key from keystore {} with alias {}. Exception thrown : {}", keystoreLocation, alias ,e);
            throw new KeyRetrievalException(e);
        }
    }

    public PrivateKey getPrivateKey() {
        return getPrivateKeyByAlias(alias);
    }

    private KeyStore loadKeyStore(final String storeLocation, final String password,
                                  final String keyStoreType)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        try (InputStream resourceAsStream = getStoreInputStream(storeLocation)) {
            final KeyStore keystore = KeyStore.getInstance(keyStoreType);
            keystore.load(resourceAsStream, password.toCharArray());
            return keystore;
        }
    }

    private InputStream getStoreInputStream(String storeLocation) throws IOException {
        File filesystemFile = new File(storeLocation);
        if (filesystemFile.exists()) {
            return new FileInputStream(filesystemFile);
        }
        String encodedKeyStore = System.getenv(storeLocation);
        if (encodedKeyStore != null) {
            return new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(
                    new String(encodedKeyStore.getBytes(StandardCharsets.UTF_8),
                            StandardCharsets.UTF_8)));
        }
        final InputStream resourceAsStream =
                this.getClass().getClassLoader().getResourceAsStream(storeLocation);
        if (resourceAsStream == null) {
            throw new IOException("No file corresponding to '" + storeLocation
                    + "' was found on the classpath, filesystem or in environment variable.");
        }
        return resourceAsStream;
    }

    public SignerPublicKey getActivePublicKey(String algorithm) {
        try {
            KeyStore keyStore = this.loadKeyStore(this.keystoreLocation, this.password, this.keyStoreType);
            return this.getSignerPublicKey(algorithm, this.alias, keyStore);
        } catch (CertificateException | IOException | KeyStoreException | NoSuchAlgorithmException var4) {
            log.error("Exception while attempting to get public key from truststore {}. Exception thrown : {}", this.keystoreLocation, var4);
            throw new KeyRetrievalException("failed to retrieve public key", var4);
        }
    }

    private SignerPublicKey getSignerPublicKey(String algorithm, String alias, KeyStore keyStore) {
        try {
            Certificate preferredCertificate = keyStore.getCertificate(alias);
            String preferredCertificateAlgo = preferredCertificate.getPublicKey().getAlgorithm();
            if (this.verifyKeyType(algorithm, preferredCertificateAlgo)) {
                log.info("building public key using preferred alias {}", alias);
                return this.buildSigningPublicKey(preferredCertificate, algorithm, alias);
            } else {
                log.warn("certificate type does not match for preferred alias {}", alias);
                Enumeration aliases = keyStore.aliases();

                while(aliases.hasMoreElements()) {
                    String certAlias = (String)aliases.nextElement();
                    Certificate certificate = keyStore.getCertificate(certAlias);
                    String certificateAlgo = certificate.getPublicKey().getAlgorithm();
                    if (this.verifyKeyType(algorithm, certificateAlgo)) {
                        log.info("building public key using certificate alias {}", certAlias);
                        return this.buildSigningPublicKey(certificate, algorithm, certAlias);
                    }

                    log.warn("certificate type does not match for certificate alias {}", certAlias);
                }

                return null;
            }
        } catch (KeyStoreException var10) {
            log.error("Exception while attempting to get active public key by alias {}. ", alias + "Exception thrown : {}", var10);
            throw new KeyRetrievalException("failed to retrieve public key", var10);
        }
    }

    private boolean verifyKeyType(String algorithm, String certAlgorithm) {
        boolean conditionOne = algorithm.equalsIgnoreCase(JWSAlgorithm.RS256.getName()) && certAlgorithm.equalsIgnoreCase("RSA");
        boolean conditionTwo = algorithm.equalsIgnoreCase(JWSAlgorithm.ES256.getName()) && certAlgorithm.equalsIgnoreCase("EC");
        return conditionOne || conditionTwo;
    }

    private SignerPublicKey buildSigningPublicKey(Certificate certificate, String algorithm, String certAlias) {
        if (!Oauth2Constants.RS256_ALGORITHM.equals(algorithm) && !Oauth2Constants.RSA_ALGORITHM.equals(algorithm)) {
            JWK jwkEc = (new ECKey.Builder(Curve.P_256, (ECPublicKey)certificate.getPublicKey())).keyUse(KeyUse.from((X509Certificate)certificate)).algorithm(JWTSupportedAlgorithm.parseJWSAlgorithm(algorithm)).keyID(certAlias).x509CertChain(this.buildCertificateChain(certificate)).build();
            return SignerPublicKey.builder().publicKey(jwkEc).active(true).build();
        } else {
            JWK jwkRsa = (new com.nimbusds.jose.jwk.RSAKey.Builder((RSAPublicKey)certificate.getPublicKey())).algorithm(JWTSupportedAlgorithm.parseJWSAlgorithm(algorithm)).keyID(certAlias).x509CertChain(this.buildCertificateChain(certificate)).build();
            return SignerPublicKey.builder().publicKey(jwkRsa).active(true).build();
        }
    }

    private List<Base64> buildCertificateChain(Certificate certificate) {
        try {
            return Collections.singletonList(Base64.encode(certificate.getEncoded()));
        } catch (CertificateEncodingException var3) {
            log.warn("error while converting client certificate {}", var3);
        }
        return null;
    }

    public void validateAlgorithm(String algorithm) {
        if (!JWSAlgorithm.RS256.getName().equals(algorithm) && !JWSAlgorithm.ES256.getName().equals(algorithm)) {
            throw new UnsupportedOperationException("Requested algorithm: {} " + algorithm + " is currently not supported for signing");
        }
    }

}
