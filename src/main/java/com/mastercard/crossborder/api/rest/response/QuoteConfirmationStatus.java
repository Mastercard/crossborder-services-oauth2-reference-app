package com.mastercard.crossborder.api.rest.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mastercard.crossborder.api.rest.request.BaseRequest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@JsonTypeName(value = "quoteconfirmationstatus")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@XmlType(name = "QuoteConfirmationStatus", propOrder = { "transactionReference", "status", "stage", "proposalId", "paymentSubmissionExpiryTime", "proposedQuote"})
@XmlRootElement(name = "quoteconfirmationstatus")
public class QuoteConfirmationStatus extends BaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String transactionReference;
    private String status;
    private String stage;
    private String proposalId;
    private String paymentSubmissionExpiryTime;
    private ProposedQuote proposedQuote;

    @JsonProperty(value = "transactionReference", required = true)
    @XmlElement(name = "transactionReference", required = true)
    @Override
    public String getTransactionReference() {
        return transactionReference;
    }

    @Override
    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    @JsonProperty(value = "proposalId")
    @XmlElement(name = "proposalId", required = true)
    public String getProposalId() {
        return proposalId;
    }

    public void setProposalId(String proposalId) {
        this.proposalId = proposalId;
    }

    @JsonProperty(value = "status")
    @XmlElement(name = "status", required = true)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty(value = "stage")
    @XmlElement(name = "stage", required = true)
    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    @JsonProperty(value = "paymentSubmissionExpiryTime")
    @XmlElement(name = "paymentSubmissionExpiryTime", required = true)
    public String getPaymentSubmissionExpiryTime() {
        return paymentSubmissionExpiryTime;
    }

    public void setPaymentSubmissionExpiryTime(String paymentSubmissionExpiryTime) {
        this.paymentSubmissionExpiryTime = paymentSubmissionExpiryTime;
    }

}
