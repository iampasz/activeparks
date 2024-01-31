package com.app.activeparks.data.model.track

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class ListTrackResponse (
    val items: List<TrackResponse>,
    val total: Long,
    val offset: Long,
    val limit: Long
)

data class TrackResponse(
    var address: String,
    val calories: String,
    var complexityId: String,
    var coverImage: String,
    var coverType: List<String>,
    val createdAt: String,
    val createdBy: String,
    val dateFinishRecord: String,
    val dateStartRecord: String,
    var description: String,
    val id: String,
    var integrity: Boolean,
    val isTrackActive: Boolean,
    var name: String,
    var photos: List<String>,
    var pointsTrack: List<PointsTrack>,
    var recommendedTime: String,
    val routeLength: String,
    val screenshot: String,
    var type: String,
    val updatedAt: String
)

data class PointsTrack(
    val latitude: Double,
    val longitude: Double,
    val turn: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(turn)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PointsTrack> {
        override fun createFromParcel(parcel: Parcel): PointsTrack {
            return PointsTrack(parcel)
        }

        override fun newArray(size: Int): Array<PointsTrack?> {
            return arrayOfNulls(size)
        }
    }
}
