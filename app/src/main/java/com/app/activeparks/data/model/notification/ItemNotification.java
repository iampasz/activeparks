package com.app.activeparks.data.model.notification;

import com.app.activeparks.data.model.user.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemNotification {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("subjectId")
    @Expose
    private String subjectId;
    @SerializedName("subjectType")
    @Expose
    private String subjectType;
    @SerializedName("objectId")
    @Expose
    private String objectId;
    @SerializedName("objectType")
    @Expose
    private String objectType;
    @SerializedName("isRead")
    @Expose
    private Boolean isRead;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("entities")
    @Expose
    private Entities entities;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("event")
    @Expose
    private EventNotification event;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Entities getEntities() {
        return entities;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EventNotification getEvent() {
        return event;
    }

    public void setEvent(EventNotification event) {
        this.event = event;
    }


    }


