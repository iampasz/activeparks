package com.app.activeparks.data.model.clubs;

import com.app.activeparks.data.model.news.CreatedBy;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemClub {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("statusId")
    @Expose
    private String statusId;
    @SerializedName("logoUrl")
    @Expose
    private String logoUrl;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("memberAmount")
    @Expose
    private Integer memberAmount;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("location")
    @Expose
    private Object location;
    @SerializedName("tagline")
    @Expose
    private String tagline;
    @SerializedName("establishedAt")
    @Expose
    private Object establishedAt;
    @SerializedName("facebookUrl")
    @Expose
    private String facebookUrl;
    @SerializedName("instagramUrl")
    @Expose
    private String instagramUrl;
    @SerializedName("youtubeUrl")
    @Expose
    private String youtubeUrl;
    @SerializedName("telegramUrl")
    @Expose
    private String telegramUrl;
    @SerializedName("socialNetworkUrl")
    @Expose
    private String socialNetworkUrl;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("clubUser")
    @Expose
    private ClubUser clubUser;
    @SerializedName("soonestEvent")
    @Expose
    private SoonestEvent soonestEvent;
    @SerializedName("createdBy")
    @Expose
    private CreatedBy createdBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMemberAmount() {
        return memberAmount;
    }

    public void setMemberAmount(Integer memberAmount) {
        this.memberAmount = memberAmount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public Object getEstablishedAt() {
        return establishedAt;
    }

    public void setEstablishedAt(Object establishedAt) {
        this.establishedAt = establishedAt;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getTelegramUrl() {
        return telegramUrl;
    }

    public void setTelegramUrl(String telegramUrl) {
        this.telegramUrl = telegramUrl;
    }


    public String getSocialNetworkUrl() {
        return socialNetworkUrl;
    }

    public void setSocialNetworkUrl(String socialNetworkUrl) {
        this.socialNetworkUrl = socialNetworkUrl;
    }

    public ClubUser getClubUser() {
        return clubUser;
    }

    public void setClubUser(ClubUser clubUser) {
        this.clubUser = clubUser;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public SoonestEvent getSoonestEvent() {
        return soonestEvent;
    }

    public void setSoonestEvent(SoonestEvent soonestEvent) {
        this.soonestEvent = soonestEvent;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }
}
