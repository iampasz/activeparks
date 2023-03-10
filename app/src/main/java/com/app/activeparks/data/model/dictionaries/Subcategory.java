package com.app.activeparks.data.model.dictionaries;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Subcategory extends BaseDictionaries{

    @PrimaryKey(autoGenerate = true)
    int key;

    @SerializedName("parentId")
    @Expose
    private String parentId;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

}
