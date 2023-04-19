package com.app.activeparks.data.model.sportevents;

import com.app.activeparks.data.model.clubs.ClubUser;
import com.app.activeparks.data.model.clubs.ItemClub;
import com.app.activeparks.data.model.event.RoutePoint;
import com.app.activeparks.data.model.news.CreatedBy;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemEvent {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("statusId")
    @Expose
    private String statusId;
    @SerializedName("typeId")
    @Expose
    private String typeId;
    @SerializedName("isPrivate")
    @Expose
    private Boolean isPrivate;
    @SerializedName("isOpenForParticipationWithoutApplying")
    @Expose
    private Boolean isOpenForParticipationWithoutApplying;
    @SerializedName("alias")
    @Expose
    private String alias;
    @SerializedName("holdingStatusId")
    @Expose
    private String holdingStatusId;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl = "";
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
    private String fullDescription;
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
    @SerializedName("meetingId")
    @Expose
    private Object meetingId;
    @SerializedName("meetingRecordsCount")
    @Expose
    private Integer meetingRecordsCount;
    @SerializedName("meetingFinishActualTime")
    @Expose
    private String meetingFinishActualTime;
    @SerializedName("meetingNotifyBeforeActualSendTime")
    @Expose
    private Object meetingNotifyBeforeActualSendTime;
    @SerializedName("meetingNotifyAfterActualSendTime")
    @Expose
    private Object meetingNotifyAfterActualSendTime;
    @SerializedName("meetingNotifyOnstartActualSendTime")
    @Expose
    private Object meetingNotifyOnstartActualSendTime;
    @SerializedName("conferenceLink")
    @Expose
    private String conferenceLink;
    @SerializedName("eventEstimation")
    @Expose
    private Integer eventEstimation;
    @SerializedName("eventHost")
    @Expose
    private String eventHost;
    @SerializedName("createdBy")
    @Expose
    private CreatedBy createdBy;
    @SerializedName("categories")
    @Expose
    private List<String> categories = null;
    @SerializedName("sportsground")
    @Expose
    private Sportsground sportsground;
    @SerializedName("eventUser")
    @Expose
    private ClubUser eventUser;
    @SerializedName("club")
    @Expose
    private ItemClub club;
    @SerializedName("routePoints")
    @Expose
    private List<RoutePoint> routePoints;

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

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Boolean getIsOpenForParticipationWithoutApplying() {
        return isOpenForParticipationWithoutApplying;
    }

    public void setIsOpenForParticipationWithoutApplying(Boolean isOpenForParticipationWithoutApplying) {
        this.isOpenForParticipationWithoutApplying = isOpenForParticipationWithoutApplying;
    }

    public ClubUser getEventUser() {
        return eventUser;
    }

    public void setEventUser(ClubUser eventUser) {
        this.eventUser = eventUser;
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

    public Object getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Object meetingId) {
        this.meetingId = meetingId;
    }

    public Integer getMeetingRecordsCount() {
        return meetingRecordsCount;
    }

    public void setMeetingRecordsCount(Integer meetingRecordsCount) {
        this.meetingRecordsCount = meetingRecordsCount;
    }

    public String getMeetingFinishActualTime() {
        return meetingFinishActualTime;
    }

    public void setMeetingFinishActualTime(String meetingFinishActualTime) {
        this.meetingFinishActualTime = meetingFinishActualTime;
    }

    public Object getMeetingNotifyBeforeActualSendTime() {
        return meetingNotifyBeforeActualSendTime;
    }

    public void setMeetingNotifyBeforeActualSendTime(Object meetingNotifyBeforeActualSendTime) {
        this.meetingNotifyBeforeActualSendTime = meetingNotifyBeforeActualSendTime;
    }

    public Object getMeetingNotifyAfterActualSendTime() {
        return meetingNotifyAfterActualSendTime;
    }

    public void setMeetingNotifyAfterActualSendTime(Object meetingNotifyAfterActualSendTime) {
        this.meetingNotifyAfterActualSendTime = meetingNotifyAfterActualSendTime;
    }

    public Object getMeetingNotifyOnstartActualSendTime() {
        return meetingNotifyOnstartActualSendTime;
    }

    public void setMeetingNotifyOnstartActualSendTime(Object meetingNotifyOnstartActualSendTime) {
        this.meetingNotifyOnstartActualSendTime = meetingNotifyOnstartActualSendTime;
    }

    public String getConferenceLink() {
        return conferenceLink;
    }

    public void setConferenceLink(String conferenceLink) {
        this.conferenceLink = conferenceLink;
    }

    public Integer getEventEstimation() {
        return eventEstimation;
    }

    public void setEventEstimation(Integer eventEstimation) {
        this.eventEstimation = eventEstimation;
    }

    public String getEventHost() {
        return eventHost;
    }

    public void setEventHost(String eventHost) {
        this.eventHost = eventHost;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Sportsground getSportsground() {
        return sportsground;
    }

    public void setSportsground(Sportsground sportsground) {
        this.sportsground = sportsground;
    }

    public ItemClub getClub() {
        return club;
    }

    public void setClub(ItemClub club) {
        this.club = club;
    }

    public List<RoutePoint> getRoutePoints() {
        return routePoints;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public void setRoutePoints(List<RoutePoint> routePoints) {
        this.routePoints = routePoints;
    }

}