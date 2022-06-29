package com.mastercard.crossborder.api.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;

@JsonPropertyOrder(value = {"request","review","response" })
public class PaymentPurpose implements Serializable{

    private static final long serialVersionUID = 1L;

    private Request request;
    private Review review;
    private Response<Object> response;

    @JsonProperty(value = "request")
    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    @JsonProperty(value = "review")
    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    @JsonProperty(value = "response")
    public Response<Object> getResponse() {
        return response;
    }

    public void setResponse(Response<Object> response) {
        this.response = response;
    }
}
