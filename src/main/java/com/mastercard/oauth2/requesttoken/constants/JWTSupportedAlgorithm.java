package com.mastercard.oauth2.requesttoken.constants;

import com.nimbusds.jose.JWSAlgorithm;

public enum JWTSupportedAlgorithm {


    RS256("RS256", "SHA-256", "rsa-sha256", "SHA256withRSA"),
    ES256("ES256", "SHA-256", "ecdsa-sha256", "SHA256withECDSA"),
    RS1("RS1", "RSA-SHA1", "rsa-sha1", "SHA1withRSA"),
    NOT_SUPPORTED(Oauth2Constants.NOT_SUPPORTED, Oauth2Constants.NOT_SUPPORTED, Oauth2Constants.NOT_SUPPORTED, Oauth2Constants.NOT_SUPPORTED);

    private final String algorithmName;
    private final String hmacAlgorithmName;
    private final String caasSupportedName;
    private final String javaSignature;


    JWTSupportedAlgorithm(String algorithmName, String hmacAlgorithmName, String caasSupportedName, String javaSignature) {
        this.algorithmName = algorithmName;
        this.hmacAlgorithmName = hmacAlgorithmName;
        this.caasSupportedName = caasSupportedName;
        this.javaSignature = javaSignature;
    }

    private String getAlgorithmName() {
        return this.algorithmName;
    }

    private String getHmacAlgorithmName() {
        return this.hmacAlgorithmName;
    }

    public String getCaasSupportedName() {
        return this.caasSupportedName;
    }

    public String getJavaSignature() {
        return this.javaSignature;
    }

    public static com.mastercard.oauth2.requesttoken.constants.JWTSupportedAlgorithm parseHmacAlgorithm(String hmac) {
        if (RS256.getHmacAlgorithmName().equals(hmac)) {
            return RS256;
        } else if (ES256.getHmacAlgorithmName().equals(hmac)) {
            return ES256;
        } else {
            return RS1.getHmacAlgorithmName().equals(hmac) ? RS1 : NOT_SUPPORTED;
        }
    }

    public static com.mastercard.oauth2.requesttoken.constants.JWTSupportedAlgorithm parse(String s) {
        if (RS256.getAlgorithmName().equals(s)) {
            return RS256;
        } else if (ES256.getAlgorithmName().equals(s)) {
            return ES256;
        } else {
            return RS1.getAlgorithmName().equals(s) ? RS1 : NOT_SUPPORTED;
        }
    }

    public static JWSAlgorithm parseJWSAlgorithm(String algName) {
        if (JWSAlgorithm.RS256.getName().equals(algName)) {
            return JWSAlgorithm.RS256;
        } else if (JWSAlgorithm.ES256.getName().equals(algName)) {
            return JWSAlgorithm.ES256;
        } else {
            return JWSAlgorithm.PS512.getName().equals(algName) ? JWSAlgorithm.PS512 : new JWSAlgorithm(algName);
        }
    }
}
