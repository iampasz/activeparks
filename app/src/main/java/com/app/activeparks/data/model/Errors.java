package com.app.activeparks.data.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Errors {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("msg")
    @Expose
    private String msg = "";

    public String getValue(){
        return value;
    }

    public String getMsg(){
        return msg;
    }

}
