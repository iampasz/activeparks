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

    @SerializedName("param")
    @Expose
    private String param = "";

    public String getValue(){
        return value;
    }

    public String getMsg(){
        return msg;
    }

    public String getParam(){
        return param;
    }

}
