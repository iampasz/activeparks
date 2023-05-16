package com.app.activeparks.data.model.workout;

import com.app.activeparks.data.model.video.VideoItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WorkoutItem {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("isActive")
    @Expose
    private Integer isActive;
    @SerializedName("activityType")
    @Expose
    private String activityType;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("videoId")
    @Expose
    private String videoId;
    @SerializedName("isOnce")
    @Expose
    private Boolean isOnce;
    @SerializedName("isOpenForAdmins")
    @Expose
    private Boolean isOpenForAdmins;
    @SerializedName("weekDay")
    @Expose
    private Integer weekDay;
    @SerializedName("createdById")
    @Expose
    private String createdById;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("finishTime")
    @Expose
    private String finishTime;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("startsAt")
    @Expose
    private String startsAt;

    @SerializedName("finishesAt")
    @Expose
    private String finishesAt;
    @SerializedName("lastUpdatedAt")
    @Expose
    private String lastUpdatedAt;
    @SerializedName("tookPart")
    @Expose
    private Boolean tookPart;
    @SerializedName("video")
    @Expose
    private VideoItem video;
    @SerializedName("exercises")
    @Expose
    private List<String> exercises;
    @SerializedName("notes")
    @Expose
    private List<NotesItem> notes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getTitle() {
        return title;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Boolean getIsOnce() {
        return isOnce;
    }

    public void setIsOnce(Boolean isOnce) {
        this.isOnce = isOnce;
    }

    public Boolean getIsOpenForAdmins() {
        return isOpenForAdmins;
    }

    public void setIsOpenForAdmins(Boolean isOpenForAdmins) {
        this.isOpenForAdmins = isOpenForAdmins;
    }

    public Integer getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(Integer weekDay) {
        this.weekDay = weekDay;
    }

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public String getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(String lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Boolean getTookPart() {
        return tookPart;
    }

    public void setTookPart(Boolean tookPart) {
        this.tookPart = tookPart;
    }

    public VideoItem getVideo() {
        return video;
    }

    public void setVideo(VideoItem video) {
        this.video = video;
    }
    public List<String> getExercises() {
        return exercises;
    }

    public void setExercises(List<String> exercises) {
        this.exercises = exercises;
    }
    public List<NotesItem> getNotes() {
        return notes;
    }

    public void setNotes(List<NotesItem> notes) {
        this.notes = notes;
    }

}