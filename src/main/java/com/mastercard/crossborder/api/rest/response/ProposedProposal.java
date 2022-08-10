package com.mastercard.crossborder.api.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Calendar;


@JsonTypeName(value = "proposedproposal")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@XmlType(name = "ProposedProposal", propOrder = { "id", "resourceType", "feesIncluded", "expirationDate", "quoteFxRate", "chargedAmount",
        "creditedAmount", "principalAmount", "additionalDataList", "confirmationExpiryTime"})
@XmlRootElement(name = "proposedproposal")
public class ProposedProposal implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String resourceType = PROPOSAL_OBJECT;
    private Boolean feesIncluded;
    private Calendar expirationDate;
    private String quoteFxRate;
    private ChargedAmount chargedAmount;
    private CreditedAmount creditedAmount;
    private PrincipalAmount principalAmount;
    private AdditionalDataList additionalDataList;
    private Calendar confirmationExpiryTime;
    private static final String PROPOSAL_OBJECT = "proposal";

    @JsonProperty(value = "id", required = true)
    @XmlElement(name = "id", required = true)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty(value = "resourceType", required = true)
    @XmlElement(name = "resourceType", required = true)
    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    @JsonProperty(value = "feesIncluded", required = true)
    @XmlElement(name = "feesIncluded", required = true)
    public Boolean getFeesIncluded() {
        return feesIncluded;
    }

    public void setFeesIncluded(Boolean feesIncluded) {
        this.feesIncluded = feesIncluded;
    }

    @JsonProperty(value = "expirationDate", required = true)
    @XmlElement(name = "expirationDate", required = true)
    public Calendar getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Calendar expirationDate) {
        this.expirationDate = expirationDate;
    }

    @JsonProperty(value = "quoteFxRate", required = true)
    @XmlElement(name = "quoteFxRate", required = true)
    public String getQuoteFxRate() {
        return quoteFxRate;
    }

    public void setQuoteFxRate(String quoteFxRate) {
        this.quoteFxRate = quoteFxRate;
    }

    @JsonProperty(value = "chargedAmount", required = true)
    @XmlElement(name = "chargedAmount", required = true)
    public ChargedAmount getChargedAmount() {
        return chargedAmount;
    }

    public void setChargedAmount(ChargedAmount chargedAmount) {
        this.chargedAmount = chargedAmount;
    }

    @JsonProperty(value = "creditedAmount", required = true)
    @XmlElement(name = "creditedAmount", required = true)
    public CreditedAmount getCreditedAmount() {
        return creditedAmount;
    }

    public void setCreditedAmount(CreditedAmount creditedAmount) {
        this.creditedAmount = creditedAmount;
    }

    @JsonProperty(value = "principalAmount", required = true)
    @XmlElement(name = "principalAmount", required = true)
    public PrincipalAmount getPrincipalAmount() {
        return principalAmount;
    }

    public void setPrincipalAmount(PrincipalAmount principalAmount) {
        this.principalAmount = principalAmount;
    }

    @JsonProperty(value = "additionalDataList", required = true)
    @XmlElement(name = "additionalDataList", required = true)
    public AdditionalDataList getAdditionalDataList() {
        return additionalDataList;
    }

    public void setAdditionalDataList(AdditionalDataList additionalDataList) {
        this.additionalDataList = additionalDataList;
    }

    @JsonProperty(value = "confirmationExpiryTime", required = true)
    @XmlElement(name = "confirmationExpiryTime", required = true)
    public Calendar getConfirmationExpiryTime() {
        return confirmationExpiryTime;
    }

    public void setConfirmationExpiryTime(Calendar confirmationExpiryTime) {
        this.confirmationExpiryTime = confirmationExpiryTime;
    }
}
