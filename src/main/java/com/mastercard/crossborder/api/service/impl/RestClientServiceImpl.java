package com.mastercard.crossborder.api.service.impl;

import com.fasterxml.jackson.databind.type.CollectionType;
import com.mastercard.crossborder.api.exception.ServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastercard.crossborder.api.config.MastercardApiConfig;
import com.mastercard.crossborder.api.rest.response.EncryptedPayload;
import com.mastercard.crossborder.api.rest.response.Errors;
import com.mastercard.crossborder.api.service.RestClientService;
import com.mastercard.oauth2.requesttoken.generator.Oauth2RequestTokenGenerator;
import com.mastercard.oauth2.requesttoken.models.TokenInput;
import com.mastercard.crossborder.api.util.EncryptionUtils;
import com.nimbusds.jose.JWSAlgorithm;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static com.mastercard.crossborder.api.constants.MastercardHttpHeaders.ENCRYPTED_HEADER;

@Component
public class RestClientServiceImpl<T> implements RestClientService<T> {

    private static final Logger logger = LoggerFactory.getLogger(RestClientServiceImpl.class);
    private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    private TransformerFactory tf = TransformerFactory.newInstance();
    private ObjectMapper mapper = new ObjectMapper();
    public static final String BALANCE_API = "/accounts";
    public static final String REQUEST_TOKEN = "Bearer";

    @Autowired
    MastercardApiConfig mastercardApiConfig;

    @Override
    public T service(String baseURL, HttpHeaders headers, HttpMethod httpMethod, Map<String, java.lang.Object> requestParams,Object request, Class<T> responseClass,boolean isListResponse,Class<T> listElementClass) throws ServiceException{

        String url = buildURL(baseURL, requestParams);

        String oAuthString;
        String requestStr = convertToString(headers, request);

        /* Generate oauth*/
        //String oAuthString = authenticate(url, httpMethod, requestStr);
        if(mastercardApiConfig.getRunAllAPIsWithAccessToken()) {
            oAuthString = mastercardApiConfig.getAccessToken(); //getRequestToken();
            validateBalanceAPICall(baseURL, oAuthString);
        } else {
            oAuthString = "Bearer " + getRequestToken();
            validateBalanceAPICall(baseURL, oAuthString);
        }

        /*Build requestEntity */
        HttpEntity<MultiValueMap<String, String>> requestEntity = generateRequestEntity(Boolean.FALSE, headers, requestStr, oAuthString);

        logger.info("Request payload : {}", requestEntity);

        /*make API call*/
        try {
            T response = callCrossBorderAPI(url, httpMethod, requestParams, requestEntity, responseClass);
            String responseLog = convertToString(headers, response);
            logger.info("Response payload : {}", responseLog);
            if(isListResponse && listElementClass!=null) {
                return (T) mapAccountValues(responseLog,listElementClass,headers,responseClass);
            }
            return response;
        }catch (HttpClientErrorException | HttpServerErrorException he){
            T errors = getContentFromString(headers, he.getResponseBodyAsString(), (Class<T>) Errors.class);
            throw new ServiceException(he.getResponseBodyAsString(), (Errors) errors);
        }

    }

    @Override
    public T service(String baseURL, HttpHeaders headers, HttpMethod httpMethod, Map<String, java.lang.Object> requestParams,Object request, Class<T> responseClass) throws ServiceException{
        return service(baseURL,headers,httpMethod,requestParams,request,responseClass,false,null);
    }

    private void validateBalanceAPICall(String baseURL, String oAuthString) throws ServiceException {
        if(baseURL.contains(BALANCE_API) &&!(isBavApi(baseURL)) && (oAuthString == null || oAuthString.contains(REQUEST_TOKEN))) {
            throw new ServiceException("To access Balance APIs, please configure Access Token in .properties file.");
        }
    }

    private boolean isBavApi(String baseURL) {
        return baseURL.contains("/generate-ibans") || baseURL.contains("/validations") || baseURL.contains("/banks/details");
    }


    @Override
    public T serviceEncryption(String baseURL, HttpHeaders headers, HttpMethod httpMethod, Map<String, java.lang.Object> requestParams, Object request, Class<T> responseClass) throws ServiceException{
        return serviceEncryption(baseURL,headers,httpMethod,requestParams,request,responseClass,false,null);
    }

    @Override
    public T serviceEncryption(String baseURL, HttpHeaders headers, HttpMethod httpMethod, Map<String, java.lang.Object> requestParams, Object request, Class<T> responseClass,boolean isListResponse,Class<T> listElementClass) throws ServiceException{

        if (mastercardApiConfig.getRunWithEncryptedPayload()) {

                String url = buildURL(baseURL, requestParams);
                String oAuthString;
                String requestStr = convertToString(headers, request);

                /*Encrypt the request payload and return */
                String requestBody = getEncryptedRequestBody(headers, requestStr);

                /* Generate oauth*/
                //String oAuthString = authenticate(url, httpMethod, requestBody);
                if (mastercardApiConfig.getRunAllAPIsWithAccessToken()) {
                    oAuthString = mastercardApiConfig.getAccessToken(); //getRequestToken();
                    validateBalanceAPICall(baseURL, oAuthString);
                } else {
                    oAuthString = "Bearer " + getRequestToken();
                    validateBalanceAPICall(baseURL, oAuthString);
                }

                /*Build requestEntity */
                HttpEntity<MultiValueMap<String, String>> requestEntity = generateRequestEntity(Boolean.TRUE, headers, requestBody, oAuthString);
                logger.info("Encrypted Request payload : {}", requestEntity);

            try {
                T response = callCrossBorderAPI(url, httpMethod, requestParams, requestEntity, EncryptedPayload.class);
                /*Decrypt the response payload and return*/
                if (null != response) {
                    logger.info("Encrypted Response payload : {}", ((EncryptedPayload) response).getData());
                    String responseStr;
                    if(isBavApi(baseURL)){
                        responseStr = EncryptionUtils.jweDecrypt(((EncryptedPayload) response).getData(), mastercardApiConfig.getKeyPassword(), mastercardApiConfig.getDecryptionKeyFile(), mastercardApiConfig.getKeyAlias());
                    }else {
                        responseStr = EncryptionUtils.jweDecrypt(((EncryptedPayload) response).getData(), mastercardApiConfig.getDecryptionKeyFile(), mastercardApiConfig.getDecryptionKeyAlias(), mastercardApiConfig.getDecryptionKeyPassword());
                    }
                    logger.info("Decrypted Response payload: {}:",responseStr);
                    if(isListResponse && listElementClass!=null) {
                        return (T) mapAccountValues(responseStr,listElementClass,requestEntity.getHeaders(),responseClass);
                    }
                    return getContentFromString(headers, responseStr, responseClass);
                }
            } catch (HttpClientErrorException he) {
                T errors = getContentFromString(headers, he.getResponseBodyAsString(), (Class<T>) Errors.class);
                throw new ServiceException(he.getResponseBodyAsString(), (Errors) errors);
            }
        }
        return null;
    }

    private String buildURL(String baseURL, Map<String, java.lang.Object> requestParams) {

        String builtURL = UriComponentsBuilder.fromHttpUrl(mastercardApiConfig.getEndPointURL() + "/" + baseURL).uriVariables(requestParams).build().toUriString();
        logger.info("requestURL : {}", builtURL);
        return builtURL;
    }

    private String getRequestToken() throws ServiceException {
        try {
            Oauth2RequestTokenGenerator oauth2RequestTokenGenerator = new Oauth2RequestTokenGenerator(mastercardApiConfig.getP12File().getFile().getAbsolutePath(), mastercardApiConfig.getKeyAlias(), mastercardApiConfig.getKeyPassword());
            TokenInput tokenInput = TokenInput.builder()
                    .consumerKey(mastercardApiConfig.getConsumerKey())
                    .tokenSigningAlgorithm(JWSAlgorithm.RS256)
                    .populateX5cTokenHeader(true)
                    .build();
            return oauth2RequestTokenGenerator.generateToken(tokenInput);

        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private HttpEntity<MultiValueMap<String, String>> generateRequestEntity(Boolean encrypt, HttpHeaders headers, String requestStr, String oAuthString) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-mc-routing", "1e8kQ4yPf8TudlhNTcXwa7vCAlAeYa98");
        httpHeaders.add(HttpHeaders.AUTHORIZATION, oAuthString);
        //if content type is not already added, use application_xml
        if (headers.containsKey(HttpHeaders.CONTENT_TYPE) && null != headers.getContentType())
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, headers.getContentType().toString());
        else
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML);

        //Below header need to be added to process the encryption request
        if (processForEncryption(encrypt)) {
            httpHeaders.add(ENCRYPTED_HEADER.toString(), "true");
        }
        return (HttpEntity<MultiValueMap<String, String>>) new HttpEntity(requestStr, httpHeaders);
    }

    private String getEncryptedRequestBody(HttpHeaders headers, String requestStr) throws ServiceException {

        String encryptedStr ;
        if (null != requestStr && processForEncryption(Boolean.TRUE) &&  headers.containsKey(HttpHeaders.CONTENT_TYPE) )
        {
            if ( null!= headers.getContentType()&& MediaType.APPLICATION_XML.equals(headers.getContentType().toString())) {
                encryptedStr = EncryptionUtils.jweEncrypt(requestStr, mastercardApiConfig.getCertificateFile(), mastercardApiConfig.getEncryptionFP(), MediaType.APPLICATION_XML,mastercardApiConfig.getDecryptionKeyAlias(),mastercardApiConfig.getDecryptionKeyPassword());
                return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                        "<encrypted_payload><data>" + encryptedStr + "</data></encrypted_payload>";
            }
            if(null != headers.getContentType() && MediaType.APPLICATION_JSON.equals(headers.getContentType().toString())) {
                encryptedStr = EncryptionUtils.jweEncrypt(requestStr, mastercardApiConfig.getCertificateFile(), mastercardApiConfig.getEncryptionFP(), MediaType.APPLICATION_JSON,mastercardApiConfig.getDecryptionKeyAlias(),mastercardApiConfig.getDecryptionKeyPassword());
                return "{\"encrypted_payload\":{\"data\":"+"\"" + encryptedStr +"\""+"}}";
            }
        }
        return null;
    }

    public RestTemplate getRestTemplate() throws ServiceException {
        RestTemplate restTemplate;
        try {

            if (null != mastercardApiConfig.getTLSFile() && mastercardApiConfig.getTLSFile().exists()) {

                KeyStore keystore = KeyStore.getInstance("PKCS12");
                char[] password = mastercardApiConfig.getTLSPassword().toCharArray();

                keystore.load(new FileInputStream(mastercardApiConfig.getTLSFile().getFile()), password);
                SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                        new SSLContextBuilder()
                                .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                                .loadKeyMaterial(keystore, password)
                                .build(),

                        NoopHostnameVerifier.INSTANCE);
                HttpClient httpClient = HttpClients.custom()
                        .setSSLSocketFactory(socketFactory)
                        .build();

                HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
                requestFactory.setHttpClient(httpClient);

                restTemplate = new RestTemplate(requestFactory);
            } else {
                restTemplate = new RestTemplate();
            }
            return restTemplate;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }


    public T callCrossBorderAPI(String url, HttpMethod httpMethod, Map<String, java.lang.Object> requestParams, HttpEntity<MultiValueMap<String, String>> requestEntity, Class responseClass) throws ServiceException {
        T response = null;
            RestTemplate restTemplate = getRestTemplate();
            switch (httpMethod) {
                case GET:
                    ResponseEntity result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseClass, requestParams);
                    response = (T) result.getBody();
                    break;
                case POST:
                    response = (T) restTemplate.postForObject(url, requestEntity, responseClass, requestParams);
                    break;
                default:
                    response = null;
            }

        return response;
    }

    private boolean processForEncryption(Boolean encrypt) {
        return encrypt && mastercardApiConfig.getRunWithEncryptedPayload();
    }

    private T getContentFromString(HttpHeaders headers, String responseBodyAsString, Class<T> responseClass) throws ServiceException {
        if (null != headers.getContentType() && MediaType.APPLICATION_XML.equals(headers.getContentType().toString())) {
            return convertStringToXMLDocument(responseBodyAsString, responseClass);
        }
        if (null != headers.getContentType() && MediaType.APPLICATION_JSON.equals(headers.getContentType().toString())) {
            return convertStringToJSON(responseBodyAsString, responseClass);
        }
        return null;

    }

    private T convertStringToXMLDocument(String xmlString, Class<T> responseClass) throws ServiceException {
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(responseClass);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (T) jaxbUnmarshaller.unmarshal(new StringReader(xmlString));
        } catch (JAXBException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private T convertStringToJSON(String jsonString, Class responseClass) throws ServiceException {
        try {
            return (T) mapper.readValue(jsonString, responseClass);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private String convertToString(HttpHeaders headers, Object data) throws ServiceException {
        if (data != null) {
            if (null != headers.getContentType() && MediaType.APPLICATION_JSON.equals(headers.getContentType().toString())) {
                return convertJsonToString(data);
            }
            if (null != headers.getContentType() && MediaType.APPLICATION_XML.equals(headers.getContentType().toString())) {
                return convertDocumentToString(data);
            }
        }
        return null;
    }

    private String convertDocumentToString(Object obj) throws ServiceException {

        if (null == obj)
            return "";
        Document document = createXMLDocument(obj);
        Transformer transformer;
        if (null != document) {
            try {
                transformer = tf.newTransformer();
                StringWriter writer = new StringWriter();
                transformer.transform(new DOMSource(document), new StreamResult(writer));
                return writer.getBuffer().toString();
            } catch (TransformerException e) {
                throw new ServiceException(e.getMessage());
            }
        }
        return "";
    }

    public String convertJsonToString(Object jsonObject) throws ServiceException {

        try {
            return mapper.writeValueAsString(jsonObject);
        } catch (JsonProcessingException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    private Document createXMLDocument(Object request) throws ServiceException {
        try {
            Document doc = dbf.newDocumentBuilder().newDocument();
            JAXBContext context = JAXBContext.newInstance(request.getClass());
            context.createMarshaller().marshal(request, doc);
            return doc;
        } catch (ParserConfigurationException | JAXBException e) {
            throw new ServiceException(e.getMessage());
        }
    }
    private List mapAccountValues(String response,Class<T> listElementClass ,HttpHeaders headers,Class<T> responseClass) throws ServiceException  {
    	if ( null != headers.getContentType() && MediaType.APPLICATION_XML.equals(headers.getContentType().toString())) {
            return (List) convertStringToXMLDocument(response,responseClass);
        }
        if ( null != headers.getContentType() && MediaType.APPLICATION_JSON.equals(headers.getContentType().toString())) {
        try {
            CollectionType listType =
                    mapper.getTypeFactory().constructCollectionType(ArrayList.class,listElementClass);
            return mapper.readValue(response, listType);
        }catch(JsonProcessingException e){
            logger.warn("Error while processing response");
        }
        }
    return null;
    }


}
