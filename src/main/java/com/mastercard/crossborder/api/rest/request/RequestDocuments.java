package com.mastercard.crossborder.api.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mastercard.crossborder.api.rest.response.Response;

public class RequestDocuments {

    private Response response;
    @JsonProperty(value = "response")
    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
