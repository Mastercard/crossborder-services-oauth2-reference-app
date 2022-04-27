package com.mastercard.oauth2.requesttoken.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.nimbusds.jose.jwk.JWK;
import lombok.Generated;

public class SignerPublicKey {

    @JsonIgnoreProperties(
        ignoreUnknown = true
    )
    @JsonTypeInfo(
        use = Id.NONE
    )
    private JWK publicKey;
    private boolean active;

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof com.mastercard.oauth2.requesttoken.models.SignerPublicKey) {
            com.mastercard.oauth2.requesttoken.models.SignerPublicKey that = (com.mastercard.oauth2.requesttoken.models.SignerPublicKey)o;
            return (null != this.publicKey && null != that.getPublicKey()) && (this.publicKey.getKeyID().equals(that.getPublicKey().getKeyID()));
        } else {
            return false;
        }
    }

    @Generated
    public static com.mastercard.oauth2.requesttoken.models.SignerPublicKey.SignerPublicKeyBuilder builder() {
        return new com.mastercard.oauth2.requesttoken.models.SignerPublicKey.SignerPublicKeyBuilder();
    }

    @Generated
    public JWK getPublicKey() {
        return this.publicKey;
    }

    @Generated
    private boolean isActive() {
        return this.active;
    }

    @Generated
    public void setPublicKey(JWK publicKey) {
        this.publicKey = publicKey;
    }

    @Generated
    public void setActive(boolean active) {
        this.active = active;
    }

    @Generated
    private SignerPublicKey(JWK publicKey, boolean active) {
        this.publicKey = publicKey;
        this.active = active;
    }

    @Generated
    public SignerPublicKey() {
    }

    @Generated
    public String toString() {
        return "SignerPublicKey(publicKey=" + this.getPublicKey() + ", active=" + this.isActive() + ")";
    }

    @Generated
    public static class SignerPublicKeyBuilder {
        @Generated
        private JWK publicKey;
        @Generated
        private boolean active;

        @Generated
        SignerPublicKeyBuilder() {
        }

        @Generated
        public com.mastercard.oauth2.requesttoken.models.SignerPublicKey.SignerPublicKeyBuilder publicKey(JWK publicKey) {
            this.publicKey = publicKey;
            return this;
        }

        @Generated
        public com.mastercard.oauth2.requesttoken.models.SignerPublicKey.SignerPublicKeyBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        @Generated
        public com.mastercard.oauth2.requesttoken.models.SignerPublicKey build() {
            return new com.mastercard.oauth2.requesttoken.models.SignerPublicKey(this.publicKey, this.active);
        }

        @Generated
        public String toString() {
            return "SignerPublicKey.SignerPublicKeyBuilder(publicKey=" + this.publicKey + ", active=" + this.active + ")";
        }
    }
}
