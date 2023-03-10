package com.app.activeparks.data.model.city;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class City {

    @SerializedName("body")
    @Expose
    private List<Body> body = null;
    @SerializedName("itemCount")
    @Expose
    private Integer itemCount;

    public List<Body> getBody() {
        return body;
    }

    public void setBody(List<Body> body) {
        this.body = body;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

}
