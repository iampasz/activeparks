package com.app.activeparks.data.model.workout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlanModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("weekDays")
    @Expose
    private String[] weekDays;

    @SerializedName("startTime")
    @Expose
    private String startTime;

    @SerializedName("finishTime")
    @Expose
    private String finishTime;

    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("isOnce")
    @Expose
    private Boolean isOnce;

    @SerializedName("type")
    @Expose
    private String type;


    public PlanModel(String id, String title, String[] weekDays, String startTime, String finishTime, String userId, Boolean isOnce, String type, Boolean isActive) {
        this.id = id;
        this.title = title;
        this.weekDays = weekDays;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.userId = userId;
        this.isOnce = isOnce;
        this.type = type;
        this.isActive = isActive;
    }

    public PlanModel(String title, String[] weekDays, String startTime, String finishTime, String userId, Boolean isOnce, String type, Boolean isActive) {
        this.title = title;
        this.weekDays = weekDays;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.userId = userId;
        this.isOnce = isOnce;
        this.type = type;
        this.isActive = isActive;
    }

    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
}
