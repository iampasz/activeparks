package com.app.activeparks.data.model.events;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventData {
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("typeId")
    private String typeId;

    @SerializedName("isPrivate")
    private boolean isPrivate;

    @SerializedName("statusId")
    private String statusId;

    @SerializedName("alias")
    private String alias;

    @SerializedName("holdingStatusId")
    private String holdingStatusId;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("routeStart")
    private String routeStart;

    @SerializedName("routeFinish")
    private String routeFinish;

    @SerializedName("shortDescription")
    private String shortDescription;

    @SerializedName("fullDescription")
    private String fullDescription;

    @SerializedName("address")
    private String address;

    @SerializedName("memberAmount")
    private int memberAmount;

    @SerializedName("startsAt")
    private String startsAt;

    @SerializedName("finishesAt")
    private String finishesAt;

    @SerializedName("notifyBefore")
    private int notifyBefore;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("meetingId")
    private String meetingId;

    @SerializedName("meetingRecordsCount")
    private int meetingRecordsCount;

    @SerializedName("meetingFinishActualTime")
    private String meetingFinishActualTime;

    @SerializedName("meetingNotifyBeforeActualSendTime")
    private String meetingNotifyBeforeActualSendTime;

    @SerializedName("meetingNotifyAfterActualSendTime")
    private String meetingNotifyAfterActualSendTime;

    @SerializedName("meetingNotifyOnstartActualSendTime")
    private String meetingNotifyOnstartActualSendTime;

    @SerializedName("eventHost")
    private String eventHost;

//    @SerializedName("createdBy")
//    private CreatedBy createdBy;

    @SerializedName("eventUser")
    private EventUser eventUser;

    @SerializedName("categories")
    private List<String> categories;

    @SerializedName("sportsground")
    private Object sportsground;

    @SerializedName("club")
    private Object club;

    @SerializedName("routePoints")
    private List<String> routePoints;


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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRouteStart() {
        return routeStart;
    }

    public void setRouteStart(String routeStart) {
        this.routeStart = routeStart;
    }

    public String getRouteFinish() {
        return routeFinish;
    }

    public void setRouteFinish(String routeFinish) {
        this.routeFinish = routeFinish;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMemberAmount() {
        return memberAmount;
    }

    public void setMemberAmount(int memberAmount) {
        this.memberAmount = memberAmount;
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

    public int getNotifyBefore() {
        return notifyBefore;
    }

    public void setNotifyBefore(int notifyBefore) {
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

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public int getMeetingRecordsCount() {
        return meetingRecordsCount;
    }

    public void setMeetingRecordsCount(int meetingRecordsCount) {
        this.meetingRecordsCount = meetingRecordsCount;
    }

    public String getMeetingFinishActualTime() {
        return meetingFinishActualTime;
    }

    public void setMeetingFinishActualTime(String meetingFinishActualTime) {
        this.meetingFinishActualTime = meetingFinishActualTime;
    }

    public String getMeetingNotifyBeforeActualSendTime() {
        return meetingNotifyBeforeActualSendTime;
    }

    public void setMeetingNotifyBeforeActualSendTime(String meetingNotifyBeforeActualSendTime) {
        this.meetingNotifyBeforeActualSendTime = meetingNotifyBeforeActualSendTime;
    }

    public String getMeetingNotifyAfterActualSendTime() {
        return meetingNotifyAfterActualSendTime;
    }

    public void setMeetingNotifyAfterActualSendTime(String meetingNotifyAfterActualSendTime) {
        this.meetingNotifyAfterActualSendTime = meetingNotifyAfterActualSendTime;
    }

    public String getMeetingNotifyOnstartActualSendTime() {
        return meetingNotifyOnstartActualSendTime;
    }

    public void setMeetingNotifyOnstartActualSendTime(String meetingNotifyOnstartActualSendTime) {
        this.meetingNotifyOnstartActualSendTime = meetingNotifyOnstartActualSendTime;
    }

    public String getEventHost() {
        return eventHost;
    }

    public void setEventHost(String eventHost) {
        this.eventHost = eventHost;
    }

    public EventUser getEventUser() {
        return eventUser;
    }

    public void setEventUser(EventUser eventUser) {
        this.eventUser = eventUser;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
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

    public List<String> getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(List<String> routePoints) {
        this.routePoints = routePoints;
    }
}