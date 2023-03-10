package com.app.activeparks.data.model.dictionaries;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class District extends BaseDictionaries{

    @PrimaryKey
    public int uid;

    @SerializedName("regionId")
    @ColumnInfo(name = "regionId")
    @Expose
    private String regionId;
    @SerializedName("districtCenter")
    @ColumnInfo(name = "districtCenter")
    @Expose
    private String districtCenter;


    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }


    public String getDistrictCenter() {
        return districtCenter;
    }

    public void setDistrictCenter(String districtCenter) {
        this.districtCenter = districtCenter;
    }

}
