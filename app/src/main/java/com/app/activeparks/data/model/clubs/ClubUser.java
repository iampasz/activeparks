package com.app.activeparks.data.model.clubs;

import com.app.activeparks.data.model.user.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClubUser {

    @SerializedName("club")
    @Expose
    private Object club;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("isCoordinator")
    @Expose
    private Boolean isCoordinator;
    @SerializedName("isActing")
    @Expose
    private Boolean isActing;
    @SerializedName("isLeading")
    @Expose
    private Boolean isLeading;
    @SerializedName("isApproved")
    @Expose
    private Boolean isApproved;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    public Object getClub() {
        return club;
    }

    public void setClub(Object club) {
        this.club = club;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getIsCoordinator() {
        return isCoordinator;
    }

    public void setIsCoordinator(Boolean isCoordinator) {
        this.isCoordinator = isCoordinator;
    }

    public Boolean getIsActing() {
        return isActing;
    }

    public void setIsActing(Boolean isActing) {
        this.isActing = isActing;
    }

    public Boolean getLeading() {
        return isLeading;
    }

    public void setLeading(Boolean isLeading) {
        this.isLeading = isLeading;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
