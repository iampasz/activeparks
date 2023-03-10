package com.app.activeparks.data.model.clubs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SoonestEvent {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("statusId")
    @Expose
    private String statusId;
    @SerializedName("alias")
    @Expose
    private String alias;
    @SerializedName("holdingStatusId")
    @Expose
    private String holdingStatusId;
    @SerializedName("imageUrl")
    @Expose
    private Object imageUrl;
    @SerializedName("routeStart")
    @Expose
    private Object routeStart;
    @SerializedName("routeFinish")
    @Expose
    private Object routeFinish;
    @SerializedName("shortDescription")
    @Expose
    private String shortDescription;
    @SerializedName("fullDescription")
    @Expose
    private Object fullDescription;
    @SerializedName("startsAt")
    @Expose
    private String startsAt;
    @SerializedName("finishesAt")
    @Expose
    private String finishesAt;
    @SerializedName("notifyBefore")
    @Expose
    private Integer notifyBefore;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("createdBy")
    @Expose
    private Object createdBy;
    @SerializedName("categories")
    @Expose
    private Object categories;
    @SerializedName("sportsground")
    @Expose
    private Object sportsground;
    @SerializedName("club")
    @Expose
    private Object club;
    @SerializedName("routePoints")
    @Expose
    private Object routePoints;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getHoldingStatusId() {
        return holdingStatusId;
    }

    public void setHoldingStatusId(String holdingStatusId) {
        this.holdingStatusId = holdingStatusId;
    }

    public Object getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Object imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Object getRouteStart() {
        return routeStart;
    }

    public void setRouteStart(Object routeStart) {
        this.routeStart = routeStart;
    }

    public Object getRouteFinish() {
        return routeFinish;
    }

    public void setRouteFinish(Object routeFinish) {
        this.routeFinish = routeFinish;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Object getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(Object fullDescription) {
        this.fullDescription = fullDescription;
    }

    public String getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(String startsAt) {
        this.startsAt = startsAt;
    }

    public String getFinishesAt() {
        return finishesAt;
    }

    public void setFinishesAt(String finishesAt) {
        this.finishesAt = finishesAt;
    }

    public Integer getNotifyBefore() {
        return notifyBefore;
    }

    public void setNotifyBefore(Integer notifyBefore) {
        this.notifyBefore = notifyBefore;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public Object getCategories() {
        return categories;
    }

    public void setCategories(Object categories) {
        this.categories = categories;
    }

    public Object getSportsground() {
        return sportsground;
    }

    public void setSportsground(Object sportsground) {
        this.sportsground = sportsground;
    }

    public Object getClub() {
        return club;
    }

    public void setClub(Object club) {
        this.club = club;
    }

    public Object getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(Object routePoints) {
        this.routePoints = routePoints;
    }

}
