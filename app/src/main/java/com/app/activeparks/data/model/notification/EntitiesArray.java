package com.app.activeparks.data.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EntitiesArray {

    @SerializedName("entityIndex")
    @Expose
    public Integer entityIndex;
    @SerializedName("entityId")
    @Expose
    public String entityId;
    @SerializedName("entityType")
    @Expose
    public String entityType;
    @SerializedName("entityTitle")
    @Expose
    public String entityTitle;

}