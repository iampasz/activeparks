package com.app.activeparks.data.model.calendar;

import com.app.activeparks.data.model.support.SupportItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CalendarModel {

    @SerializedName("items")
    @Expose
    private List<CalendarItem> items = null;

    public List<CalendarItem> getItems() {
        return items;
    }

    public void setItems(List<CalendarItem> items) {
        this.items = items;
    }
}
