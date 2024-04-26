package com.mastercard.crossborder.api.rest.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@JsonTypeName(value = "quoteconfirmationrequest")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@XmlType(name = "QuoteConfirmationRequest", propOrder = { "transactionReference", "proposalId"})
@XmlRootElement(name = "quoteconfirmationrequest")
public class QuoteConfirmationRequest extends BaseRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String transactionReference;
    private String proposalId;

    @JsonProperty(value = "transactionReference", required = true)
    @XmlElement(name = "transactionReference", required = true)
    public String getTransactionReference() {
        return transactionReference;
    }

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

}
