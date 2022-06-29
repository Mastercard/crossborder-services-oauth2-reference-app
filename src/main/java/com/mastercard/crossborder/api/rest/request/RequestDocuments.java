package com.mastercard.crossborder.api.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mastercard.crossborder.api.rest.response.Response;

import java.io.Serializable;

public class RequestDocuments implements Serializable {

    private Response<Object> response;
    @JsonProperty(value = "response")
    public Response<Object> getResponse() {
        return response;
    }

    public void setResponse(Response<Object> response) {
        this.response = response;
    }
}
