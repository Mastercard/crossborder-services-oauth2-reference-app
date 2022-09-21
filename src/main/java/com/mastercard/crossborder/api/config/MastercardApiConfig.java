package com.mastercard.crossborder.api.config;

import com.mastercard.crossborder.api.exception.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import javax.annotation.PostConstruct;

@Configuration
@PropertySource("mastercard-api.properties")
@ComponentScan(basePackages = {"com.mastercard.crossborder"})
public class MastercardApiConfig {

    @Value("${mastercard.api.authentication.consumerKey}")
    private String consumerKey;

    @Value("${mastercard.api.authentication.keystore.keyalias}")
    private String keyAlias;

    @Value("${mastercard.api.authentication.keystore.password}")
    private String keyPassword;

    @Value("${mastercard.api.authentication.keystore.keyFile}")
    private Resource p12File;

    @Value("${mastercard.api.authentication.keystore.mtlsFile}")
    private Resource mtlsFile;

    @Value("${mastercard.api.authentication.keystore.mtlsPassword}")
    private String mtlsPassword;

    @Value("${mastercard.api.authentication.keystore.keyStoreType}")
    private String keyStoreType;

    @Value("${mastercard.api.authentication.keystore.signatureType}")
    private String signatureType;

    @Value("${mastercard.api.environment.sandbox.endPointURL}")
    private String sandboxEndPointURL;

    @Value("${mastercard.api.environment.sandbox.partnerId}")
    private String sandboxPartnerId;

    @Value("${mastercard.api.environment.runWithEncryptedPayload}")
    private Boolean runWithEncryptedPayload;

    @Value("${mastercard.api.encryption.certificateFile}")
    private Resource certificateFile;

    @Value("${mastercard.api.encryption.fingerPrint}")
    private String encryptionFP;

    @Value("${mastercard.api.decryption.keyFile}")
    private Resource decryptionKeyFile;

    @Value("${mastercard.api.environment.runAllAPIsWithAccessToken}")
    private Boolean runAllAPIsWithAccessToken;

    @Value("${mastercard.api.environment.oauth2.accessToken}")
    private String accessToken;

    @Value("${mastercard.api.authentication.keystore.keyFile:}")
    private String keystoreLocation;

    @Value("${mastercard.api.authentication.keystore.keyalias:}")
    private String alias;

    @Value("${mastercard.api.authentication.keystore.password:}")
    private String password;

    @Value("${mastercard.api.authentication.keystore.keyStoreType:}")
    private String keyStore;

    @PostConstruct
    public void setupApiConfiguration() throws ServiceException {
        if(null == p12File || StringUtils.isEmpty( consumerKey))
            throw new ServiceException(".p12 file or consumerKey does not exist, please add details in mastercard-api.properties");
        if ( getRunWithEncryptedPayload().booleanValue() && ( StringUtils.isEmpty(encryptionFP) || null == certificateFile || null == decryptionKeyFile))
            throw new ServiceException("Key parameters required for encryption are not set, please add details in mastercard-api.properties");
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getKeyAlias() {
        return keyAlias;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public Resource getP12File() {
        return p12File;
    }

    public Resource getMTLSFile() {
        return mtlsFile;
    }

    public String getMTLSPassword() {
        return mtlsPassword;
    }

    public String getEndPointURL() {
        return sandboxEndPointURL;
    }
    public String getPartnerId()  {
        return sandboxPartnerId;
    }

    public Boolean getRunWithEncryptedPayload() {
        return (null != runWithEncryptedPayload && runWithEncryptedPayload) || Boolean.FALSE ;
    }

    public Resource getCertificateFile() {
        return certificateFile;
    }

    public String getEncryptionFP() {
        return encryptionFP;
    }

    public Resource getDecryptionKeyFile() {
        return decryptionKeyFile;
    }

    public String getKeyStoreType() {
        return keyStoreType;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public Boolean getRunAllAPIsWithAccessToken() {
        return runAllAPIsWithAccessToken;
    }

    public void setRunAllAPIsWithAccessToken(Boolean runAllAPIsWithAccessToken) {
        this.runAllAPIsWithAccessToken = runAllAPIsWithAccessToken;
    }

    public String getKeystoreLocation() {
        return keystoreLocation;
    }

    public void setKeystoreLocation(String keystoreLocation) {
        this.keystoreLocation = keystoreLocation;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
