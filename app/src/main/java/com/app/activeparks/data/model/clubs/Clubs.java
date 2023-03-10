package com.app.activeparks.data.model.clubs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Clubs {

    @SerializedName("items")
    @Expose
    private List<ItemClub> items;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("offset")
    @Expose
    private Integer offset;
    @SerializedName("limit")
    @Expose
    private Integer limit;

    public List<ItemClub> getItems() {
        return items;
    }

    public void setItems(List<ItemClub> items) {
        this.items = items;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

}