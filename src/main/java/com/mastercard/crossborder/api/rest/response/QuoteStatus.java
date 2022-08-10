package com.mastercard.crossborder.api.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.Calendar;

@JsonTypeName(value = "quotestatus")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@XmlType(name = "QuoteStatus", propOrder = { "status", "statusUpdateTimestamp", "pendingStage", "errorCode", "errorMessage"})
@XmlRootElement(name = "quotestatus")
public class QuoteStatus implements Serializable {
  private static final long serialVersionUID = 1L;

  private String status;
  private Calendar statusUpdateTimestamp;
  private String pendingStage;
  private String errorCode;
  private String errorMessage;

  @JsonProperty(value = "statusUpdateTimestamp", required = true)
  @XmlElement(name = "statusUpdateTimestamp", required = true)
  public Calendar getStatusUpdateTimestamp() {
    return statusUpdateTimestamp;
  }

  public void setStatusUpdateTimestamp(Calendar statusUpdateTimestamp) {
    this.statusUpdateTimestamp = statusUpdateTimestamp;
  }

  @JsonProperty(value = "pendingStage")
  @XmlElement(name = "pendingStage", required = true)
  public String getPendingStage() {
    return pendingStage;
  }

  public void setPendingStage(String pendingStage) {
    this.pendingStage = pendingStage;
  }

  @JsonProperty(value = "status")
  @XmlElement(name = "status", required = true)
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @JsonProperty(value = "errorCode")
  @XmlElement(name = "errorCode", required = true)
  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  @JsonProperty(value = "errorMessage")
  @XmlElement(name = "errorMessage", required = true)
  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }
}

