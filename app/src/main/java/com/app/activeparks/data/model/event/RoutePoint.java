package com.app.activeparks.data.model.event;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RoutePoint implements Comparable<RoutePoint>{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("eventId")
    @Expose
    private String eventId;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("pointIndex")
    @Expose
    private Integer pointIndex;
    @SerializedName("location")
    @Expose
    private List<Double> location = null;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("timePassed")
    @Expose
    private String timePassed;
    @SerializedName("qrCode")
    @Expose
    private String qrCode;
    @SerializedName("isPassed")
    @Expose
    private Boolean passedPoints;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPointIndex() {
        return pointIndex;
    }

    public void setPointIndex(Integer pointIndex) {
        this.pointIndex = pointIndex;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getTimePassed() {
        return timePassed;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getPassedPoints() {
        return passedPoints;
    }

    public void setPassedPoints(Boolean passedPoints) {
        this.passedPoints = passedPoints;
    }

    @Override
    public int compareTo(RoutePoint o) {
        return Integer.compare(this.pointIndex, o.pointIndex);
    }
}