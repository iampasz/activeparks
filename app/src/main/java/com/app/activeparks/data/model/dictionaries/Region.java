package com.app.activeparks.data.model.dictionaries;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Region extends BaseDictionaries{


    @SerializedName("alterTitle")
    @Expose
    private String alterTitle;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("districts")
    @Expose
    private List<Object> districts = null;

    public String getAlterTitle() {
        return alterTitle;
    }

    public void setAlterTitle(String alterTitle) {
        this.alterTitle = alterTitle;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Object> getDistricts() {
        return districts;
    }

    public void setDistricts(List<Object> districts) {
        this.districts = districts;
    }

}