package com.app.activeparks.data.model.dictionaries;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BaseDictionaries {

    @SerializedName("id")
    @ColumnInfo(name = "id")
    @Expose
    private String id = "";

    @SerializedName("title")
    @ColumnInfo(name = "title")
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