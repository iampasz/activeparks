package com.app.activeparks.data.model.sportevents

import com.app.activeparks.data.model.clubs.ClubUser
import com.app.activeparks.data.model.clubs.ItemClub
import com.app.activeparks.data.model.points.RoutePoint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ItemEvent(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("statusId")
    @Expose
    var statusId: String? = null,

    @SerializedName("typeId")
    @Expose
    var typeId: String? = null,

    @SerializedName("type_id")
    @Expose
    var typeIdNew: String? = null,

    @SerializedName("isPrivate")
    @Expose
    var isPrivate: Boolean? = null,

    @SerializedName("isOpenForParticipationWithoutApplying")
    @Expose
    var isOpenForParticipationWithoutApplying: Boolean? = null,

    @SerializedName("alias")
    @Expose
    var alias: String? = null,

    @SerializedName("holdingStatusId")
    @Expose
    var holdingStatusId: String? = null,

    var holdingStatusText: String? = null,

    @SerializedName("imageUrl")
    @Expose
    var imageUrl: String? = null,

    @SerializedName("image_url")
    @Expose
    var imageUrlNew: String? = null,

    @SerializedName("routeStartedAt")
    @Expose
    var routeStartedAt: String? = null,

    @SerializedName("routeFinish")
    @Expose
    var routeFinish: Any? = null,

    @SerializedName("shortDescription")
    @Expose
    var shortDescription: String? = null,

    @SerializedName("fullDescription")
    @Expose
    var fullDescription: String? = null,

    @SerializedName("startsAt")
    @Expose
    var startsAt: String? = null,

    @SerializedName("finishesAt")
    @Expose
    var finishesAt: String? = null,

    @SerializedName("notifyBefore")
    @Expose
    var notifyBefore: Int? = null,

    @SerializedName("memberAmount")
    @Expose
    var memberAmount: Int? = null,

    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null,

    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null,

    @SerializedName("meetingId")
    @Expose
    var meetingId: Any? = null,

    @SerializedName("meetingRecordsCount")
    @Expose
    var meetingRecordsCount: Int? = null,

    @SerializedName("meetingFinishActualTime")
    @Expose
    var meetingFinishActualTime: String? = null,

    @SerializedName("meetingNotifyBeforeActualSendTime")
    @Expose
    var meetingNotifyBeforeActualSendTime: Any? = null,

    @SerializedName("meetingNotifyAfterActualSendTime")
    @Expose
    var meetingNotifyAfterActualSendTime: Any? = null,

    @SerializedName("meetingNotifyOnstartActualSendTime")
    @Expose
    var meetingNotifyOnstartActualSendTime: Any? = null,

    @SerializedName("conferenceLink")
    @Expose
    var conferenceLink: String? = null,

    @SerializedName("startAdressPoint")
    @Expose
    var startAdressPoint: String? = null,

    @SerializedName("eventEstimation")
    @Expose
    var eventEstimation: Int? = null,

    @SerializedName("timeZoneDifference")
    @Expose
    var timeZoneDifference: Long? = null,

    @SerializedName("distanceToPoint")
    @Expose
    var distanceToPoint: Float? = null,

    @SerializedName("eventHost")
    @Expose
    var eventHost: String? = null,

    @SerializedName("createdBy")
    @Expose
    var createdBy: CreatedBy? = null,

    @SerializedName("categories")
    @Expose
    var categories: List<String>? = null,

    @SerializedName("sportsground")
    @Expose
    var sportsground: Sportsground? = null,

    @SerializedName("eventUser")
    @Expose
    var eventUser: ClubUser? = null,

    @SerializedName("club")
    @Expose
    var club: ItemClub? = null,

    @SerializedName("routePoints")
    @Expose
    var routePoints: List<RoutePoint>? = null
)