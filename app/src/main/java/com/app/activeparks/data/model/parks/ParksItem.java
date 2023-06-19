package com.app.activeparks.data.model.parks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParksItem {

    public String title;
    public String description;

    public ParksItem(String title, String description){
        this.title = title;
        this.description = description;
    }
}
