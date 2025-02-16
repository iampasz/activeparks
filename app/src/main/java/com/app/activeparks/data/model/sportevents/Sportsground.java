package com.app.activeparks.data.model.sportevents;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sportsground {

    @SerializedName("id")
    @Expose
    private String id = "";
    @SerializedName("title")
    @Expose
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
