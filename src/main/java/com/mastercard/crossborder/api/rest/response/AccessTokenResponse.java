package com.mastercard.crossborder.api.rest.response;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mastercard.crossborder.api.rest.response.accountbalances.BalanceDetails;

import java.io.Serializable;
import java.util.Date;

@JsonPropertyOrder(value = { "access_token", "token_type", "expires_in","scope", "id_token", "refresh_token", "refresh_token_expires_in"})
public class AccessTokenResponse implements Serializable {
    private String access_token;
    private String token_type;

    @Override
    public String toString() {
        return "AccessTokenResponse{" +
                "access_token='" + access_token + '\'' +
                ", token_type='" + token_type + '\'' +
                ", expires_in=" + expires_in +
                ", id_token='" + id_token + '\'' +
                ", refresh_token='" + refresh_token + '\'' +
                ", refresh_token_expires_in=" + refresh_token_expires_in +
                '}';
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public Date getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Date expires_in) {
        this.expires_in = expires_in;
    }

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Date getRefresh_token_expires_in() {
        return refresh_token_expires_in;
    }

    public void setRefresh_token_expires_in(Date refresh_token_expires_in) {
        this.refresh_token_expires_in = refresh_token_expires_in;
    }

    private Date expires_in;
    private String id_token;
    private String refresh_token;
    private Date refresh_token_expires_in;



}
