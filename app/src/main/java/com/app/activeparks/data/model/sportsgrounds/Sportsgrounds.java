package com.app.activeparks.data.model.sportsgrounds;

import com.app.activeparks.data.model.routeActive.RouteActiveResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Sportsgrounds {

    @SerializedName("items")
    @Expose
    public List<ItemSportsground> items = null;
    @SerializedName("itemsActive")
    @Expose
    public List<RouteActiveResponse> itemsActive = null;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("offset")
    @Expose
    private Integer offset;
    @SerializedName("limit")
    @Expose
    private Integer limit;

    public List<ItemSportsground> getSportsground() {
        return items;
    }

    public List<RouteActiveResponse> getActiveRouter() {
        return itemsActive;
    }

//    public void setSportsground(List<ItemSportsground> items) {
//        this.items = items;
//    }

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