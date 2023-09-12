package com.app.activeparks.data.model.qr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScanerResultModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;

    public String getId() {
        return id;
    }

    public String getCategoryId() {
        return categoryId;
    }
}
