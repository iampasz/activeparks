package com.app.activeparks.data.model.location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {
    @SerializedName("house_number")
    @Expose
    private String houseNumber;
    @SerializedName("road")
    @Expose
    private String road;
    @SerializedName("borough")
    @Expose
    private String borough;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("ISO3166-2-lvl4")
    @Expose
    private String iSO31662Lvl4;
    @SerializedName("postcode")
    @Expose
    private String postcode;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("country_code")
    @Expose
    private String countryCode;

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getBorough() {
        return borough;
    }

    public void setBorough(String borough) {
        this.borough = borough;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getISO31662Lvl4() {
        return iSO31662Lvl4;
    }

    public void setISO31662Lvl4(String iSO31662Lvl4) {
        this.iSO31662Lvl4 = iSO31662Lvl4;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

}
