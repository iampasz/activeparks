package com.app.activeparks.data.model.dictionaries;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExerciseSubcategory extends BaseDictionaries {


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
