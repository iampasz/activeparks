package com.app.activeparks.data.model.clubs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClubsUserId {

    @SerializedName("userIsMember")
    @Expose
    private List<ItemClub> userIsMember;

    public List<ItemClub> getUserIsMember() {
        return userIsMember;
    }

    @SerializedName("userIsHead")
    @Expose
    private List<ItemClub> userIsHead;

    public List<ItemClub> getUserIsHead() {
        return userIsHead;
    }

}
