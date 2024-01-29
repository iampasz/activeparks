package com.app.activeparks.data.model.track

import com.google.gson.annotations.SerializedName

data class AddTrackResponse(

        @SerializedName("id")
        var id: String? = "",
        @SerializedName("name")
        var name: String? = "",
        @SerializedName("description")
        var description: String? = "",
        @SerializedName("routeLength")
        var routeLength: String? = "",
        @SerializedName("complexity")
        var complexity: String? = "",
        @SerializedName("type")
        var type: String? = "",
        @SerializedName("recommendations")
        var recommendations: String? = "",
        @SerializedName("coverType")
        var coverType: String? = "",
        @SerializedName("integrity")
        var integrity: String? = "",
        @SerializedName("address")
        var address: String? = "",
        @SerializedName("dateFinishRecord")
        var dateFinishRecord: String? = "",
        @SerializedName("dateStartRecord")
        var dateStartRecord: String? = "",
        @SerializedName("pointsTrack") var pointsTrack: ArrayList<PointsTrack> = arrayListOf()
)