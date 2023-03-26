package com.app.activeparks.data.model;

import com.app.activeparks.data.model.Errors;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MessageObject {

    @SerializedName("msg")
    @Expose
    private String msg;

    public String getMsg(){
        return msg;
    }
}
