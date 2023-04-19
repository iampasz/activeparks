package com.app.activeparks.data.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("roleId")
    @Expose
    private String roleId = "";
    @SerializedName("nickname")
    @Expose
    private String nickname = "";
    @SerializedName("firstName")
    @Expose
    private String firstName = "";
    @SerializedName("secondName")
    @Expose
    private String secondName = "";
    @SerializedName("lastName")
    @Expose
    private String lastName = "";
    @SerializedName("sex")
    @Expose
    private String sex = "";
    @SerializedName("height")
    @Expose
    private String height = "";
    @SerializedName("weight")
    @Expose
    private String weight = "";
    @SerializedName("healthState")
    @Expose
    private String healthState = "";
    @SerializedName("aboutMe")
    @Expose
    private String aboutMe = "";
    @SerializedName("hideBodyInfo")
    @Expose
    private Integer hideBodyInfo;
    @SerializedName("birthday")
    @Expose
    private String birthday = "";

    @SerializedName("age")
    @Expose
    private Integer age = 0;
    @SerializedName("phone")
    @Expose
    private String phone = "";
    @SerializedName("regionId")
    @Expose
    private String regionId = "";
    @SerializedName("districtId")
    @Expose
    private String districtId = "";
    @SerializedName("city")
    @Expose
    private String city = "";
    @SerializedName("photo")
    @Expose
    private String photo = "";
    @SerializedName("email")
    @Expose
    private String email = "";
    @SerializedName("position")
    @Expose
    private String position = "";
    @SerializedName("isActive")
    @Expose
    private Boolean isActive = false;
    @SerializedName("isPhoneVerified")
    @Expose
    private Boolean isPhoneVerified = false;
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted = false;
    @SerializedName("createdAt")
    @Expose
    private String createdAt = "";
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt = "";
    @SerializedName("regions")
    @Expose
    private List<Object> regions = null;
    @SerializedName("defaultPassword")
    @Expose
    private Boolean defaultPassword;
    @SerializedName("averageEventEstimation")
    @Expose
    private Object averageEventEstimation;
    @SerializedName("profileFilling")
    @Expose
    private Integer profileFilling = 0;
    @SerializedName("userClub")
    @Expose
    private Object userClub;
    @SerializedName("userSportEvents")
    @Expose
    private Object userSportEvents;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHealthState() {
        return healthState;
    }

    public void setHealthState(String healthState) {
        this.healthState = healthState;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }
    public String getBirthday() {
        return birthday;
    }

    public Integer getAge() {
        return age;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsPhoneVerified() {
        return isPhoneVerified;
    }

    public void setIsPhoneVerified(Boolean isPhoneVerified) {
        this.isPhoneVerified = isPhoneVerified;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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

    public List<Object> getRegions() {
        return regions;
    }

    public void setRegions(List<Object> regions) {
        this.regions = regions;
    }

    public Boolean getDefaultPassword() {
        return defaultPassword;
    }

    public void setDefaultPassword(Boolean defaultPassword) {
        this.defaultPassword = defaultPassword;
    }

    public Object getAverageEventEstimation() {
        return averageEventEstimation;
    }

    public void setAverageEventEstimation(Object averageEventEstimation) {
        this.averageEventEstimation = averageEventEstimation;
    }

    public Integer getProfileFilling() {
        return profileFilling;
    }

    public void setProfileFilling(Integer profileFilling) {
        this.profileFilling = profileFilling;
    }

    public Object getUserClub() {
        return userClub;
    }

    public void setUserClub(Object userClub) {
        this.userClub = userClub;
    }

    public Object getUserSportEvents() {
        return userSportEvents;
    }

    public void setUserSportEvents(Object userSportEvents) {
        this.userSportEvents = userSportEvents;
    }
}
