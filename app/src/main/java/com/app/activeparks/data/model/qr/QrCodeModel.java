package com.app.activeparks.data.model.qr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QrCodeModel {

    @SerializedName("clubId")
    @Expose
    private String clubId;
    @SerializedName("title")
    @Expose
    private String sportEventId;
    @SerializedName("qrCode")
    @Expose
    private String qrCode;

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public String getSportEventId() {
        return sportEventId;
    }

    public void setSportEventId(String sportEventId) {
        this.sportEventId = sportEventId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
