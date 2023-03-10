package com.app.activeparks.data.model.uservideo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserVideoItem {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title = "---";
    @SerializedName("mainPhoto")
    @Expose
    private String mainPhoto;
    @SerializedName("duration")
    @Expose
    private Object duration;
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
    private String description = "---";
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("subcategoryId")
    @Expose
    private String subcategoryId;
    @SerializedName("exerciseDifficultyLevelId")
    @Expose
    private String exerciseDifficultyLevelId;
    @SerializedName("priority")
    @Expose
    private Boolean priority;
    @SerializedName("regions")
    @Expose
    private List<Object> regions = null;
    @SerializedName("resolution")
    @Expose
    private String resolution;
    @SerializedName("url")
    @Expose
    private String url = "";
    @SerializedName("sourceUrl")
    @Expose
    private String sourceUrl;
    @SerializedName("sourceType")
    @Expose
    private Object sourceType;
    @SerializedName("sourceHost")
    @Expose
    private String sourceHost;
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
    @SerializedName("isLiked")
    @Expose
    private Boolean isLiked;

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

    public Object getDuration() {
        return duration;
    }

    public void setDuration(Object duration) {
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

    public String getExerciseDifficultyLevelId() {
        return exerciseDifficultyLevelId;
    }

    public void setExerciseDifficultyLevelId(String exerciseDifficultyLevelId) {
        this.exerciseDifficultyLevelId = exerciseDifficultyLevelId;
    }

    public Boolean getPriority() {
        return priority;
    }

    public void setPriority(Boolean priority) {
        this.priority = priority;
    }

    public List<Object> getRegions() {
        return regions;
    }

    public void setRegions(List<Object> regions) {
        this.regions = regions;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Object getSourceType() {
        return sourceType;
    }

    public void setSourceType(Object sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceHost() {
        return sourceHost;
    }

    public void setSourceHost(String sourceHost) {
        this.sourceHost = sourceHost;
    }

    public List<Object> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Object> equipment) {
        this.equipment = equipment;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
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

    public Boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
    }
}
