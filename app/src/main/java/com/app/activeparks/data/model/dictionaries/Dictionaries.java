package com.app.activeparks.data.model.dictionaries;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Dictionaries {

    @SerializedName("Districts")
    @Expose
    private List<District> districts;
    @SerializedName("CoordinatorStatuses")
    @Expose
    private List<BaseDictionaries> coordinatorStatuses = null;
    @SerializedName("ExerciseCategories")
    @Expose
    private List<ExerciseCategory> exerciseCategories = null;
    @SerializedName("ExerciseDifficultyLevels")
    @Expose
    private List<BaseDictionaries> exerciseDifficultyLevels = null;
    @SerializedName("ExerciseSubcategories")
    @Expose
    private List<ExerciseSubcategory> exerciseSubcategories = null;
    @SerializedName("FileTypes")
    @Expose
    private List<FileType> fileTypes = null;
    @SerializedName("FitnessEquipment")
    @Expose
    private List<BaseDictionaries> fitnessEquipment = null;
    @SerializedName("OwnershipTypes")
    @Expose
    private List<BaseDictionaries> ownershipTypes = null;
    @SerializedName("SportsgroundAccessTypes")
    @Expose
    private List<BaseDictionaries> sportsgroundAccessTypes = null;
    @SerializedName("SportsgroundCapacities")
    @Expose
    private List<BaseDictionaries> sportsgroundCapacities = null;
    @SerializedName("SportsgroundConditions")
    @Expose
    private List<BaseDictionaries> sportsgroundConditions = null;
    @SerializedName("SportsgroundStatuses")
    @Expose
    private List<BaseDictionaries> sportsgroundStatuses = null;
    @SerializedName("SportsgroundTypes")
    @Expose
    private List<BaseDictionaries> sportsgroundTypes = null;
    @SerializedName("Regions")
    @Expose
    private List<Region> regions = null;
    @SerializedName("UserRoles")
    @Expose
    private List<BaseDictionaries> userRoles = null;
    @SerializedName("VideoStatuses")
    @Expose
    private List<BaseDictionaries> videoStatuses = null;
    @SerializedName("EventCategories")
    @Expose
    private List<BaseDictionaries> eventCategories = null;
    @SerializedName("SportEventStatuses")
    @Expose
    private List<BaseDictionaries> sportEventStatuses = null;
    @SerializedName("InfoPageStatuses")
    @Expose
    private List<BaseDictionaries> infoPageStatuses = null;
    @SerializedName("SupportTopics")
    @Expose
    private List<BaseDictionaries> supportTopics = null;
    @SerializedName("SupportTicketStatuses")
    @Expose
    private List<BaseDictionaries> supportTicketStatuses = null;
    @SerializedName("ClubStatuses")
    @Expose
    private List<BaseDictionaries> clubStatuses = null;
    @SerializedName("EventHoldingStatuses")
    @Expose
    private List<BaseDictionaries> eventHoldingStatuses = null;

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    public List<BaseDictionaries> getCoordinatorStatuses() {
        return coordinatorStatuses;
    }

    public void setCoordinatorStatuses(List<BaseDictionaries> coordinatorStatuses) {
        this.coordinatorStatuses = coordinatorStatuses;
    }

    public List<ExerciseCategory> getExerciseCategories() {
        return exerciseCategories;
    }

    public void setExerciseCategories(List<ExerciseCategory> exerciseCategories) {
        this.exerciseCategories = exerciseCategories;
    }

    public List<BaseDictionaries> getExerciseDifficultyLevels() {
        return exerciseDifficultyLevels;
    }

    public void setExerciseDifficultyLevels(List<BaseDictionaries> exerciseDifficultyLevels) {
        this.exerciseDifficultyLevels = exerciseDifficultyLevels;
    }

    public List<ExerciseSubcategory> getExerciseSubcategories() {
        return exerciseSubcategories;
    }

    public void setExerciseSubcategories(List<ExerciseSubcategory> exerciseSubcategories) {
        this.exerciseSubcategories = exerciseSubcategories;
    }

    public List<FileType> getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(List<FileType> fileTypes) {
        this.fileTypes = fileTypes;
    }

    public List<BaseDictionaries> getFitnessEquipment() {
        return fitnessEquipment;
    }

    public void setFitnessEquipment(List<BaseDictionaries> fitnessEquipment) {
        this.fitnessEquipment = fitnessEquipment;
    }

    public List<BaseDictionaries> getOwnershipTypes() {
        return ownershipTypes;
    }

    public void setOwnershipTypes(List<BaseDictionaries> ownershipTypes) {
        this.ownershipTypes = ownershipTypes;
    }

    public List<BaseDictionaries> getSportsgroundAccessTypes() {
        return sportsgroundAccessTypes;
    }

    public void setSportsgroundAccessTypes(List<BaseDictionaries> sportsgroundAccessTypes) {
        this.sportsgroundAccessTypes = sportsgroundAccessTypes;
    }

    public List<BaseDictionaries> getSportsgroundCapacities() {
        return sportsgroundCapacities;
    }

    public void setSportsgroundCapacities(List<BaseDictionaries> sportsgroundCapacities) {
        this.sportsgroundCapacities = sportsgroundCapacities;
    }

    public List<BaseDictionaries> getSportsgroundConditions() {
        return sportsgroundConditions;
    }

    public void setSportsgroundConditions(List<BaseDictionaries> sportsgroundConditions) {
        this.sportsgroundConditions = sportsgroundConditions;
    }

    public List<BaseDictionaries> getSportsgroundStatuses() {
        return sportsgroundStatuses;
    }

    public void setSportsgroundStatuses(List<BaseDictionaries> sportsgroundStatuses) {
        this.sportsgroundStatuses = sportsgroundStatuses;
    }

    public List<BaseDictionaries> getSportsgroundTypes() {
        return sportsgroundTypes;
    }

    public void setSportsgroundTypes(List<BaseDictionaries> sportsgroundTypes) {
        this.sportsgroundTypes = sportsgroundTypes;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public List<BaseDictionaries> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<BaseDictionaries> userRoles) {
        this.userRoles = userRoles;
    }

    public List<BaseDictionaries> getVideoStatuses() {
        return videoStatuses;
    }

    public void setVideoStatuses(List<BaseDictionaries> videoStatuses) {
        this.videoStatuses = videoStatuses;
    }

    public List<BaseDictionaries> getEventCategories() {
        return eventCategories;
    }

    public void setEventCategories(List<BaseDictionaries> eventCategories) {
        this.eventCategories = eventCategories;
    }

    public List<BaseDictionaries> getSportEventStatuses() {
        return sportEventStatuses;
    }

    public void setSportEventStatuses(List<BaseDictionaries> sportEventStatuses) {
        this.sportEventStatuses = sportEventStatuses;
    }

    public List<BaseDictionaries> getInfoPageStatuses() {
        return infoPageStatuses;
    }

    public void setInfoPageStatuses(List<BaseDictionaries> infoPageStatuses) {
        this.infoPageStatuses = infoPageStatuses;
    }

    public List<BaseDictionaries> getSupportTopics() {
        return supportTopics;
    }

    public void setSupportTopics(List<BaseDictionaries> supportTopics) {
        this.supportTopics = supportTopics;
    }

    public List<BaseDictionaries> getSupportTicketStatuses() {
        return supportTicketStatuses;
    }

    public void setSupportTicketStatuses(List<BaseDictionaries> supportTicketStatuses) {
        this.supportTicketStatuses = supportTicketStatuses;
    }

    public List<BaseDictionaries> getClubStatuses() {
        return clubStatuses;
    }

    public void setClubStatuses(List<BaseDictionaries> clubStatuses) {
        this.clubStatuses = clubStatuses;
    }

    public List<BaseDictionaries> getEventHoldingStatuses() {
        return eventHoldingStatuses;
    }

    public void setEventHoldingStatuses(List<BaseDictionaries> eventHoldingStatuses) {
        this.eventHoldingStatuses = eventHoldingStatuses;
    }

}
