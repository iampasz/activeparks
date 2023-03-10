package com.app.activeparks.data.model.sportevents;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SportEvents {

    @SerializedName("items")
    @Expose
    private List<ItemEvent> items = null;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("offset")
    @Expose
    private Integer offset;
    @SerializedName("limit")
    @Expose
    private Integer limit;

    public List<ItemEvent> getItems() {
        return items;
    }

    public SportEvents setItems(List<ItemEvent> items) {
        this.items = items;
        return this;
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
