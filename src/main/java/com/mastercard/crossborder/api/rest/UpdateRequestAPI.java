package com.mastercard.crossborder.api.rest;
import com.mastercard.crossborder.api.exception.ServiceException;
import com.mastercard.crossborder.api.rest.request.UpdateRequest;
import com.mastercard.crossborder.api.rest.response.UpdateResponse;
import com.mastercard.crossborder.api.service.RestClientService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class UpdateRequestAPI {
    private static final Logger logger = LoggerFactory.getLogger(UpdateRequestAPI.class);

    public static final String UPDATE_REQUEST = "/send/partners/{partner_id}/crossborder/rfi/requests/{request_id}";

    private final RestClientService restClientService;

    public UpdateResponse updateRequest(HttpHeaders headers, Map<String, Object> requestParams, UpdateRequest updateRequest) throws ServiceException {

        logger.info("Calling UpdateRequest API");
        return (UpdateResponse) restClientService.service(UPDATE_REQUEST, headers, HttpMethod.POST, requestParams, updateRequest, UpdateResponse.class);
    }
    public UpdateResponse updateRequestWithEncryption(HttpHeaders headers, Map<String, Object> requestParams, UpdateRequest updateRequest) throws ServiceException {

        logger.info("Calling UpdateRequest API with Encryption");
        return (UpdateResponse) restClientService.serviceEncryption(UPDATE_REQUEST, headers, HttpMethod.POST, requestParams, updateRequest, UpdateResponse.class);
    }

}



