package com.mastercard.crossborder.api.rest;

import com.mastercard.crossborder.api.exception.ServiceException;
import com.mastercard.crossborder.api.rest.response.RetrieveResponse;
import com.mastercard.crossborder.api.service.RestClientService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RetrieveRequestAPI {
    private static final Logger logger = LoggerFactory.getLogger(RetrieveRequestAPI.class);

    public static final String RETRIEVE_REQUEST = "/send/partners/{partner_id}/crossborder/rfi/requests/{request_id}";

    private final RestClientService restClientService;

    public RetrieveResponse getRequestById(HttpHeaders headers, Map<String, Object> requestParams) throws ServiceException {

        logger.info("Calling RetrieveRequest by Request ID API");
        return (RetrieveResponse) restClientService.service(RETRIEVE_REQUEST, headers, HttpMethod.GET, requestParams,null, RetrieveResponse.class);
    }


    public RetrieveResponse getRequestByIdWithEncryption(HttpHeaders headers, Map<String, Object> requestParams) throws ServiceException {

        logger.info("Calling RetrieveRequest with Encryption by Request ID API");
        return (RetrieveResponse) restClientService.serviceEncryption(RETRIEVE_REQUEST, headers, HttpMethod.GET, requestParams, null, RetrieveResponse.class);
    }
}
