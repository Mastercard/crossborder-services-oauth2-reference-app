package com.mastercard.oauth2.requesttoken.generator;

import com.mastercard.oauth2.requesttoken.exception.PublicKeyRequestException;
import com.mastercard.oauth2.requesttoken.exception.TokenGenerationException;
import com.mastercard.oauth2.requesttoken.models.SignerPublicKey;
import com.mastercard.oauth2.requesttoken.models.TokenInput;
import com.mastercard.oauth2.requesttoken.service.LocalCertificateProvider;
import com.mastercard.oauth2.requesttoken.service.LocalTokenSignerService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Oauth2RequestTokenGenerator {

    private static final Logger log = LoggerFactory.getLogger(com.mastercard.oauth2.requesttoken.generator.Oauth2RequestTokenGenerator.class);
    public static final String PKCS_12 = "PKCS12";

    private LocalTokenSignerService localTokenSignerService ;
    private LocalCertificateProvider certificateProvider;

    public static final String NOT_AVAILABLE = "not_available";

    public Oauth2RequestTokenGenerator(String oAuth2KeyFile, String oAuth2keyAlias, String oAuth2KeyPassword) {
        this.certificateProvider = new LocalCertificateProvider(
                oAuth2KeyFile,
                oAuth2keyAlias,
                oAuth2KeyPassword,
                PKCS_12);
        this.localTokenSignerService = new LocalTokenSignerService(this.certificateProvider);
    }

    public String generateToken(TokenInput tokenInput) {

        final Date createdDate = new Date();
        final Date dateNotAfter = new Date(createdDate.getTime() + tokenInput.getTokenLifetime());
        SignerPublicKey signerPublicKey;

        try {
            signerPublicKey = fetchActivePublicKey(tokenInput.getTokenSigningAlgorithm().getName());
        } catch (Exception e) {
            throw new TokenGenerationException("Unable to retrieve valid Public Key for signer");
        }

        JWSHeader jwsHeader = new JWSHeader(tokenInput.getTokenSigningAlgorithm(),
                JOSEObjectType.JWT,
                "JWS",
                null,
                null,
                null,
                null,
                null,
                buildKeyThumbprint(signerPublicKey),
                buildTokenSigningCertificateChain(tokenInput, signerPublicKey),
                buildKeyId(tokenInput, signerPublicKey),
                true,
                null,
                null);

        return initialiseClaimSet(dateNotAfter, createdDate, jwsHeader);
    }

    private String initialiseClaimSet(Date dateNotAfter, Date createdDate, JWSHeader jwsHeader) {
        String clientAssertionToken = null;
        {
            try {
                JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                        .expirationTime(dateNotAfter)
                        .notBeforeTime(createdDate)
                        .issueTime(createdDate)
                        .jwtID(new MD5Generator().generateValue()).build();

                // construct a JWT payloadCheckSumUtil
                Payload payload = new Payload(claimsSet.toJSONObject());
                JWSObject jwsObject = new JWSObject(jwsHeader, payload);
                clientAssertionToken = localTokenSignerService.sign(jwsObject);

            } catch (OAuthSystemException e) {
                e.printStackTrace();
            }
        }
        return clientAssertionToken;
    }

    private SignerPublicKey fetchActivePublicKey(String algorithm) {
        this.certificateProvider.validateAlgorithm(algorithm);
        SignerPublicKey signerPublicKey = this.certificateProvider.getActivePublicKey(algorithm);
        if (!ObjectUtils.isEmpty(signerPublicKey) && !ObjectUtils.isEmpty(signerPublicKey.getPublicKey())) {
            return signerPublicKey;
        } else {
            throw new PublicKeyRequestException("No Active Public Key found for algorithm: " + algorithm);
        }
    }

    private Base64URL buildKeyThumbprint(SignerPublicKey signerPublicKey) {
        JWK key = signerPublicKey.getPublicKey();
        try {
            return key.computeThumbprint();
        } catch (JOSEException e) {
           throw new TokenGenerationException("Error during token generation: Failed during computing thumbprint");
        }
    }

    private String buildKeyId(final TokenInput tokenInput, final SignerPublicKey signerPublicKey) {
        return StringUtils.hasText(tokenInput.getConsumerKey()) ? tokenInput.getConsumerKey() : signerPublicKey.getPublicKey().getKeyID();
    }

    private List<Base64> buildTokenSigningCertificateChain(final TokenInput tokenInput, final SignerPublicKey signerPublicKey) {
        if (!tokenInput.isPopulateX5cTokenHeader() || CollectionUtils.isEmpty(signerPublicKey.getPublicKey().getX509CertChain())) {
            return Collections.emptyList();
        }

        // To reduce the token size, token header - 'x5c' to carry just signing certificate NOT sub-ca and root-ca
        return Collections.singletonList(signerPublicKey.getPublicKey().getX509CertChain().get(0));
    }


}
