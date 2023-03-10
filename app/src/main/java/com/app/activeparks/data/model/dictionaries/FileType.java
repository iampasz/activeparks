package com.app.activeparks.data.model.dictionaries;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileType extends BaseDictionaries{


    @SerializedName("type")
    @Expose
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
