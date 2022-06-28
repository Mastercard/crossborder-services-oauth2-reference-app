package com.mastercard.crossborder.api.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

@JsonTypeName(value = "proposedquote")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@XmlType(name = "ProposedQuote", propOrder = { "proposalId", "confirmStatus", "cancelStatus", "releasedReservedAmount", "statusUpdateTimestamp", "paymentSubmissionExpiryTime", "proposals", "paymentType"})
@XmlRootElement(name = "proposedquote")
public class ProposedQuote implements Serializable {

  private static final long serialVersionUID = 1L;
  private String proposalId;
  private QuoteStatus confirmStatus;
  private QuoteStatus cancelStatus;
  private ReleasedReversedAmount releasedReservedAmount;
  private Calendar statusUpdateTimestamp;
  private Calendar paymentSubmissionExpiryTime;
  private List<ProposedProposal> proposals;
  private String paymentType;

  @JsonProperty(value = "statusUpdateTimestamp", required = true)
  @XmlElement(name = "statusUpdateTimestamp", required = true)
  public Calendar getStatusUpdateTimestamp() {
    return statusUpdateTimestamp;
  }

  public void setStatusUpdateTimestamp(Calendar statusUpdateTimestamp) {
    this.statusUpdateTimestamp = statusUpdateTimestamp;
  }

  @JsonProperty(value = "proposalId")
  @XmlElement(name = "proposalId", required = true)
  public String getProposalId() {
    return proposalId;
  }

  public void setProposalId(String proposalId) {
    this.proposalId = proposalId;
  }

  @JsonProperty(value = "confirmStatus")
  @XmlElement(name = "confirmStatus", required = true)
  public QuoteStatus getConfirmStatus() {
    return confirmStatus;
  }

  public void setConfirmStatus(QuoteStatus confirmStatus) {
    this.confirmStatus = confirmStatus;
  }

  @JsonProperty(value = "cancelStatus")
  @XmlElement(name = "cancelStatus", required = true)
  public QuoteStatus getCancelStatus() {
    return cancelStatus;
  }

  public void setCancelStatus(QuoteStatus cancelStatus) {
    this.cancelStatus = cancelStatus;
  }

  @JsonProperty(value = "releasedReservedAmount")
  @XmlElement(name = "releasedReservedAmount", required = true)
  public ReleasedReversedAmount getReleasedReservedAmount() {
    return releasedReservedAmount;
  }

  public void setReleasedReservedAmount(ReleasedReversedAmount releasedReservedAmount) {
    this.releasedReservedAmount = releasedReservedAmount;
  }

  @JsonProperty(value = "paymentSubmissionExpiryTime", required = true)
  @XmlElement(name = "paymentSubmissionExpiryTime", required = true)
  public Calendar getPaymentSubmissionExpiryTime() {
    return paymentSubmissionExpiryTime;
  }

  public void setPaymentSubmissionExpiryTime(Calendar paymentSubmissionExpiryTime) {
    this.paymentSubmissionExpiryTime = paymentSubmissionExpiryTime;
  }
}

