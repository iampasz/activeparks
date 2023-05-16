package com.app.activeparks.data.model.location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("amenity")
    @Expose
    private String amenity;
    @SerializedName("road")
    @Expose
    private String road;
    @SerializedName("neighbourhood")
    @Expose
    private String neighbourhood;
    @SerializedName("suburb")
    @Expose
    private String suburb;
    @SerializedName("town")
    @Expose
    private String town;
    @SerializedName("municipality")
    @Expose
    private String municipality;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("ISO3166-2-lvl4")
    @Expose
    private String ISO3166;
    @SerializedName("postcode")
    @Expose
    private String postcode;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("country_code")
    @Expose
    private String countryCode;


    // Getter Methods

    public String getAmenity() {
        return amenity;
    }

    public String getCity() {
        return city;
    }

    public String getRoad() {
        return road;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public String getSuburb() {
        return suburb;
    }

    public String getTown() {
        return town;
    }

    public String getMunicipality() {
        return municipality;
    }

    public String getDistrict() {
        return district;
    }

    public String getState() {
        return state;
    }

    public String getISO3166() {
        return ISO3166;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    // Setter Methods

    public void setCity(String city) {
        this.city = city;
    }
    public void setAmenity(String amenity) {
        this.amenity = amenity;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setISO3166(String ISO3166 ) {
        this.ISO3166 = ISO3166;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}