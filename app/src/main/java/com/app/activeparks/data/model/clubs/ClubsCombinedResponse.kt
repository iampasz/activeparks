package com.app.activeparks.data.model.clubs

data class ClubsCombinedResponse(
    val items: ClubsItems,
    val publishedTotal: Int,
    val offset: Int,
    val limit: Int
)

data class ClubsItems(
    val userIsHead: List<ItemClub>,
    val userIsMember: List<ItemClub>,
    val published: List<ItemClub>
)


//data class ClubsCombinedResponse(
//
//
//    @SerializedName("items")
//    val items: TypesOfClubs?,
//
//    @SerializedName("publishedTotal")
//    val publishedTotal: Int?,
//
//    @SerializedName("offset")
//    val offset: Int?,
//
//    @SerializedName("limit")
//    val limit: Int?
//)

//data class TypesOfClubs(
//    @SerializedName("userIsHead")
//    val userIsHead: List<ClubItem>?,
//
//    @SerializedName("userIsMember")
//    val userIsMember: List<ClubItem>?,
//
//    @SerializedName("published")
//    val published: List<ClubItem>?
//)
//
//data class ClubItem(
//
//    @SerializedName("id")
//    val id: String?,
//
//    @SerializedName("originalId")
//    val originalId: String?,
//
//    @SerializedName("name")
//    val name: String?,
//
//    @SerializedName("statusId")
//    val statusId: String?,
//
//    @SerializedName("isTest")
//    val isTest: Int?,
//
//    @SerializedName("logoUrl")
//    val logoUrl: String?,
//
//    @SerializedName("description")
//    val description: String?,
//
//    @SerializedName("memberAmount")
//    val memberAmount: Int?,
//
//    @SerializedName("phone")
//    val phone: String?,
//
//    @SerializedName("address")
//    val address: String?,
//
//    @SerializedName("location")
//    val location: List<Double>?,
//
//    @SerializedName("tagline")
//    val tagline: String?,
//
//    @SerializedName("socialNetworkUrl")
//    val socialNetworkUrl: String?,
//
//    @SerializedName("createdAt")
//    val createdAt: String?,
//
//    @SerializedName("distanceToPoint")
//    val distanceToPoint: Double?,
//
//    @SerializedName("clubUser")
//    val clubUser: ClubUser?,
//
//    @SerializedName("soonestEvent")
//    val soonestEvent: SoonestEvent?,
//
//    @SerializedName("createdBy")
//    val createdBy: CreatedBy?
//)


