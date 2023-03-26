package com.app.activeparks.data.model.authorisation;

import com.app.activeparks.data.model.Errors;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Signup {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("roleId")
    @Expose
    private String roleId;
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("secondName")
    @Expose
    private String secondName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("sex")
    @Expose
    private Object sex;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("photo")
    @Expose
    private Object photo;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;
    @SerializedName("isPhoneVerified")
    @Expose
    private Boolean isPhoneVerified;
    @SerializedName("isDeleted")
    @Expose
    private Boolean isDeleted;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("regions")
    @Expose
    private List<Object> regions = null;

    @SerializedName("errors")
    @Expose
    private List<Errors> errors;

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

    public Object getSex() {
        return sex;
    }

    public void setSex(Object sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getPhoto() {
        return photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public Boolean getIsPhoneVerified() {
        return isPhoneVerified;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public List<Object> getRegions() {
        return regions;
    }

    public String getError() {
        return error;
    }

    public List<Errors> getErrors() {
        return errors;
    }

}
