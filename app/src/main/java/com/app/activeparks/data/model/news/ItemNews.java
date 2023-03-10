package com.app.activeparks.data.model.news;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ItemNews {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title = "";
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("body")
    @Expose
    private String body = "";
    @SerializedName("statusId")
    @Expose
    private String statusId = "";
    @SerializedName("publishedAt")
    @Expose
    private String publishedAt = " ";
    @SerializedName("createdAt")
    @Expose
    private String createdAt = " ";
    @SerializedName("photos")
    @Expose
    private List<String> photos = new ArrayList<>();
    @SerializedName("createdBy")
    @Expose
    private CreatedBy createdBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public List<String> getPhotos() {
        return photos;
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
