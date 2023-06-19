package com.app.activeparks.data.model.sportsgrounds;

import com.app.activeparks.data.model.sportevents.SportEvents;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemSportsground {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("originalId")
    @Expose
    private Object originalId;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("location")
    @Expose
    private List<Double> location;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("statusId")
    @Expose
    private String statusId;
    @SerializedName("typeId")
    @Expose
    private String typeId;
    @SerializedName("ownershipTypeId")
    @Expose
    private String ownershipTypeId;
    @SerializedName("accessTypeId")
    @Expose
    private String accessTypeId;
    @SerializedName("conditionId")
    @Expose
    private String conditionId;
    @SerializedName("capacityId")
    @Expose
    private String capacityId;
    @SerializedName("districtId")
    @Expose
    private String districtId;
    @SerializedName("regionId")
    @Expose
    private String regionId;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("ownerName")
    @Expose
    private String ownerName;
    @SerializedName("ownerPhone")
    @Expose
    private String ownerPhone;
    @SerializedName("ownerDetails")
    @Expose
    private String ownerDetails;
    @SerializedName("registryCertificate")
    @Expose
    private String registryCertificate;
    @SerializedName("ownershipCertificate")
    @Expose
    private String ownershipCertificate;
    @SerializedName("hasLighting")
    @Expose
    private Integer hasLighting;
    @SerializedName("onReconstruction")
    @Expose
    private Boolean onReconstruction;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("workHours")
    @Expose
    private String workHours;
    @SerializedName("facebookUrl")
    @Expose
    private String facebookUrl;
    @SerializedName("rating")
    @Expose
    private Object rating;
    @SerializedName("coordinatorsCount")
    @Expose
    private Integer coordinatorsCount;
    @SerializedName("messagesCount")
    @Expose
    private Integer messagesCount;
    @SerializedName("distanceToPoint")
    @Expose
    private Double distanceToPoint = 0.0;
    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("updatedBy")
    @Expose
    private String updatedBy;
    @SerializedName("deletedBy")
    @Expose
    private Object deletedBy;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("deletedAt")
    @Expose
    private Object deletedAt;
    @SerializedName("sportEvents")
    @Expose
    private List<SportEvents> sportEvents;
    @SerializedName("photos")
    @Expose
    private List<String> photos = null;
    @SerializedName("fitnessEquipment")
    @Expose
    private List<Object> fitnessEquipment;
    @SerializedName("coordinators")
    @Expose
    private List<Coordinator> coordinators;
    @SerializedName("owner")
    @Expose
    private Owner owner;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Object originalId) {
        this.originalId = originalId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getOwnershipTypeId() {
        return ownershipTypeId;
    }

    public void setOwnershipTypeId(String ownershipTypeId) {
        this.ownershipTypeId = ownershipTypeId;
    }

    public String getAccessTypeId() {
        return accessTypeId;
    }

    public void setAccessTypeId(String accessTypeId) {
        this.accessTypeId = accessTypeId;
    }

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
    }

    public String getCapacityId() {
        return capacityId;
    }

    public void setCapacityId(String capacityId) {
        this.capacityId = capacityId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public String getOwnerDetails() {
        return ownerDetails;
    }

    public void setOwnerDetails(String ownerDetails) {
        this.ownerDetails = ownerDetails;
    }

    public String getRegistryCertificate() {
        return registryCertificate;
    }

    public void setRegistryCertificate(String registryCertificate) {
        this.registryCertificate = registryCertificate;
    }

    public String getOwnershipCertificate() {
        return ownershipCertificate;
    }

    public void setOwnershipCertificate(String ownershipCertificate) {
        this.ownershipCertificate = ownershipCertificate;
    }

    public Integer getHasLighting() {
        return hasLighting;
    }

    public void setHasLighting(Integer hasLighting) {
        this.hasLighting = hasLighting;
    }

    public Boolean getOnReconstruction() {
        return onReconstruction;
    }

    public void setOnReconstruction(Boolean onReconstruction) {
        this.onReconstruction = onReconstruction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWorkHours() {
        return workHours;
    }

    public void setWorkHours(String workHours) {
        this.workHours = workHours;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public Object getRating() {
        return rating;
    }

    public void setRating(Object rating) {
        this.rating = rating;
    }

    public Integer getCoordinatorsCount() {
        return coordinatorsCount;
    }

    public void setCoordinatorsCount(Integer coordinatorsCount) {
        this.coordinatorsCount = coordinatorsCount;
    }

    public Integer getMessagesCount() {
        return messagesCount;
    }

    public void setMessagesCount(Integer messagesCount) {
        this.messagesCount = messagesCount;
    }

    public Double getDistanceToPoint() {
        return distanceToPoint;
    }

    public void setDistanceToPoint(Double distanceToPoint) {
        this.distanceToPoint = distanceToPoint;
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

    public Object getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(Object deletedBy) {
        this.deletedBy = deletedBy;
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

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public List<SportEvents> getSportEvents() {
        return sportEvents;
    }

    public void setSportEvents(List<SportEvents> sportEvents) {
        this.sportEvents = sportEvents;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public List<Object> getFitnessEquipment() {
        return fitnessEquipment;
    }

    public void setFitnessEquipment(List<Object> fitnessEquipment) {
        this.fitnessEquipment = fitnessEquipment;
    }

    public List<Coordinator> getCoordinators() {
        return coordinators;
    }

    public void setCoordinators(List<Coordinator> coordinators) {
        this.coordinators = coordinators;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

}
