package com.app.activeparks.data.model.video;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class VideoItem {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("mainPhoto")
    @Expose
    private String mainPhoto;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("likes")
    @Expose
    private Integer likes;
    @SerializedName("views")
    @Expose
    private Integer views;
    @SerializedName("statusId")
    @Expose
    private String statusId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("subcategoryId")
    @Expose
    private String subcategoryId;
    @SerializedName("fileTypeId")
    @Expose
    private String fileTypeId;
    @SerializedName("exerciseDifficultyLevelId")
    @Expose
    private String exerciseDifficultyLevelId;
    @SerializedName("messagesCount")
    @Expose
    private Integer messagesCount;
    @SerializedName("priority")
    @Expose
    private Boolean priority;
    @SerializedName("regions")
    @Expose
    private List<String> regions = null;
    @SerializedName("equipment")
    @Expose
    private List<Object> equipment = null;
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("updatedBy")
    @Expose
    private String updatedBy;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("files")
    @Expose
    private Files files;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("isLiked")
    @Expose
    private Object isLiked;

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

    public String getMainPhoto() {
        return mainPhoto;
    }

    public void setMainPhoto(String mainPhoto) {
        this.mainPhoto = mainPhoto;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getFileTypeId() {
        return fileTypeId;
    }

    public void setFileTypeId(String fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public String getExerciseDifficultyLevelId() {
        return exerciseDifficultyLevelId;
    }

    public void setExerciseDifficultyLevelId(String exerciseDifficultyLevelId) {
        this.exerciseDifficultyLevelId = exerciseDifficultyLevelId;
    }

    public Integer getMessagesCount() {
        return messagesCount;
    }

    public void setMessagesCount(Integer messagesCount) {
        this.messagesCount = messagesCount;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Files getFiles() {
        return files;
    }

    public String  getUrl() {
        return url;
    }

    public void setFiles(Files files) {
        this.files = files;
    }

    public List<String> getRegions() {
        return regions;
    }

    public void setRegions(List<String> regions) {
        this.regions = regions;
    }

    public List<Object> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Object> equipment) {
        this.equipment = equipment;
    }

    public Object getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Object isLiked) {
        this.isLiked = isLiked;
    }

}