package com.app.activeparks.data.model.clubs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClubsUserIsMemberModel {

    @SerializedName("items")
    @Expose
    private ClubsUserId items;

    public ClubsUserId getItems() {
        return items;
    }

}