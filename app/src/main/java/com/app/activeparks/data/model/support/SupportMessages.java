package com.app.activeparks.data.model.support;

import com.app.activeparks.data.model.news.CreatedBy;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SupportMessages {

    @SerializedName("id")
    @Expose
    private String id = "";
    @SerializedName("supportTicketId")
    @Expose
    private String supportTicketId  = "";
    @SerializedName("text")
    @Expose
    private String text  = "";
    @SerializedName("createdAt")
    @Expose
    private String createdAt  = "";
    @SerializedName("createdBy")
    @Expose
    private CreatedBy createdBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSupportTicketId() {
        return supportTicketId;
    }

    public void setSupportTicketId(String supportTicketId) {
        this.supportTicketId = supportTicketId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }
}
