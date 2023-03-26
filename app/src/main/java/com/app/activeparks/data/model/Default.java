package com.app.activeparks.data.model;

import com.app.activeparks.data.model.city.Body;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Default {

    public Default(boolean status){
        this.status = status;
    }

    public Default(String error){
        this.error = error;
    }

    private boolean status = false;

    @SerializedName("error")
    @Expose
    private String error;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("lastRequestTimestamp")
    @Expose
    private String lastRequestTimestamp;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("errors")
    @Expose
    private List<Errors> errors;

    public boolean getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getUrl() {
        return url;
    }

    public String getMessage() {
        return message;
    }

    public String getLastRequestTimestamp() {
        return lastRequestTimestamp;
    }

    public List<Errors> getErrors() {
        return errors;
    }

}