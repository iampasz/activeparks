package com.app.activeparks.data.model.news;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsClubs {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("clubId")
    @Expose
    private String clubId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("photos")
    @Expose
    private Object photos;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Object getPhotos() {
        return photos;
    }

    public void setPhotos(Object photos) {
        this.photos = photos;
    }


}
