package com.app.activeparks.data.model.calendar;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CalendarItem {

    @SerializedName("data")
    @Expose
    private String data;
    @SerializedName("typeId")
    @Expose
    private String typeId = "";

    public String data() {
        return data;
    }

    public String typeId() {
        return typeId;
    }
}
