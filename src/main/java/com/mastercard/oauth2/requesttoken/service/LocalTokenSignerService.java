package com.mastercard.oauth2.requesttoken.service;

import com.mastercard.oauth2.requesttoken.exception.TokenGenerationException;
import com.mastercard.oauth2.requesttoken.service.LocalCertificateProvider;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.Curve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.util.Objects;

public class LocalTokenSignerService {

    private static final Logger log = LoggerFactory.getLogger(com.mastercard.oauth2.requesttoken.service.LocalTokenSignerService.class);

    private static final String ERROR_MESSAGE = "No private key associated with alias ";

    private final LocalCertificateProvider certificateProvider;

    public LocalTokenSignerService(LocalCertificateProvider certificateProvider) {
        this.certificateProvider = certificateProvider;
        log.info("Initialized Local Token Signer Service");
    }

    public String sign(JWSObject jwsObject) {
        JWSAlgorithm jwsAlgorithm = jwsObject.getHeader().getAlgorithm();
        if (!JWSAlgorithm.RS256.equals(jwsAlgorithm) && !JWSAlgorithm.ES256.equals(jwsAlgorithm)) {
            throw new UnsupportedOperationException("Requested algorithm: " + jwsAlgorithm.getName() + " is currently not supported for signing");
        }

        PrivateKey privateKey = readPrivateKey();

        try {
            JWSSigner signer;

            if(JWSAlgorithm.RS256.equals(jwsAlgorithm)) {
                signer = new RSASSASigner(privateKey);
            } else {
                signer = new ECDSASigner(privateKey, Curve.P_256);
            }

            log.info("using signer " + signer);

            jwsObject.sign(signer);
            return jwsObject.serialize();

        } catch (JOSEException e) {
            log.error("Error occurred during self signing of Token: " + e.getMessage());
            throw new TokenGenerationException("Error occurred during token signing");
        }
    }

    private PrivateKey readPrivateKey() {
        // read private key for 'kid' header in jwt token
        final PrivateKey privateKeyForKeyId = certificateProvider.getPrivateKey();

        if(Objects.nonNull(privateKeyForKeyId)) {
            return privateKeyForKeyId;
        }
        throw new TokenGenerationException("Failed to retrieve private key to sign a token");
    }
}
