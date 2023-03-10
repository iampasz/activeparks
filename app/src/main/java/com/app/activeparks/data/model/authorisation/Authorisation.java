package com.app.activeparks.data.model.authorisation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Authorisation {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("refreshToken")
    @Expose
    private String refreshToken;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("payload")
    @Expose
    private Payload payload;

    public String getToken() {
        return token;
    }

    public String getError() {
        return error;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

}