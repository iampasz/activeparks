package com.app.activeparks.data.model.support;

import com.app.activeparks.data.model.news.CreatedBy;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SupportItem {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("topic")
    @Expose
    private String topic = "";
    @SerializedName("statusId")
    @Expose
    private String statusId = "";
    @SerializedName("text")
    @Expose
    private String text = "";
    @SerializedName("lastStatusChange")
    @Expose
    private String lastStatusChange = "";
    @SerializedName("createdAt")
    @Expose
    private String createdAt = "";
    @SerializedName("messages")
    @Expose
    private List<SupportMessages> messages;
    @SerializedName("createdBy")
    @Expose
    private CreatedBy createdBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLastStatusChange() {
        return lastStatusChange;
    }

    public void setLastStatusChange(String lastStatusChange) {
        this.lastStatusChange = lastStatusChange;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<SupportMessages> getMessages() {
        return messages;
    }

    public void setMessages(List<SupportMessages> messages) {
        this.messages = messages;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }
}
