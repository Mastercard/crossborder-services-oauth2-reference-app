package com.mastercard.crossborder.api.rest;

import com.mastercard.crossborder.api.exception.ServiceException;
import com.mastercard.crossborder.api.rest.request.UploadDocumentRequest;
import com.mastercard.crossborder.api.rest.response.UploadDocumentResponse;
import com.mastercard.crossborder.api.service.RestClientService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Map;

/*
    This class is to make a call to UploadDocument API.
*/
@RequiredArgsConstructor
@Component
public class UploadDocumentAPI {
    private static final Logger logger = LoggerFactory.getLogger(UploadDocumentAPI.class);

    public static final String UPLOAD_DOCUMENT = "/send/partners/{partner_id}/crossborder/rfi/documents";

    private final RestClientService restClientService;

    public UploadDocumentResponse uploadDocument(HttpHeaders headers, Map<String, Object> requestParams, UploadDocumentRequest uploadDocumentRequest) throws ServiceException {

        logger.info("Calling UploadDocument API");
        return (UploadDocumentResponse) restClientService.service(UPLOAD_DOCUMENT, headers, HttpMethod.POST, requestParams, uploadDocumentRequest, UploadDocumentResponse.class);
    }
    public UploadDocumentResponse uploadDocumentWithEncryption(HttpHeaders headers, Map<String, Object> requestParams, UploadDocumentRequest uploadDocumentRequest) throws ServiceException {

        logger.info("Calling UploadDocument API with Encryption");
        return (UploadDocumentResponse) restClientService.serviceEncryption(UPLOAD_DOCUMENT, headers, HttpMethod.POST, requestParams, uploadDocumentRequest, UploadDocumentResponse.class);
    }

}
