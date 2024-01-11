package com.app.activeparks.data.model.registration

import com.google.gson.annotations.SerializedName

data class User(

	@SerializedName("birthday")
	val birthday: String? = null,

	@SerializedName("lastName")
	val lastName: String? = null,

	@SerializedName("regions")
	val regions: List<String>? = null,

	@SerializedName("city")
	val city: String? = null,

	@SerializedName("bankIdToken")
	val bankIdToken: String? = null,

	@SerializedName("subRole")
	val subRole: Boolean? = null,

	@SerializedName("isVeteran")
	val isVeteran: Int? = null,

	@SerializedName("isActive")
	val isActive: Boolean? = null,

	@SerializedName("averageEventEstimation")
	val averageEventEstimation: Any? = null,

	@SerializedName("userSportEvents")
	val userSportEvents: Any? = null,

	@SerializedName("aboutMe")
	val aboutMe: String? = null,

	@SerializedName("createdAt")
	val createdAt: String? = null,

	@SerializedName("isDeleted")
	val isDeleted: Boolean? = null,

	@SerializedName("userClubPermission")
	val userClubPermission: Any? = null,

	@SerializedName("heartRateZonesUser")
	val heartRateZonesUser: Any? = null,

	@SerializedName("isVpo")
	val isVpo: Int? = null,

	@SerializedName("permissions")
	val permissions: List<String?>? = null,

	@SerializedName("nickname")
	val nickname: String? = null,

	@SerializedName("imageBackground")
	val imageBackground: String? = null,

	@SerializedName("id")
	val id: String,

	@SerializedName("email")
	val email: String? = null,

	@SerializedName("defaultPassword")
	val defaultPassword: Boolean? = null,

	@SerializedName("secondName")
	val secondName: String? = null,

	@SerializedName("height")
	val height: Int? = null,

	@SerializedName("updatedAt")
	val updatedAt: String? = null,

	@SerializedName("isPhoneVerified")
	val isPhoneVerified: Boolean? = null,

	@SerializedName("roleId")
	val roleId: String? = null,

	@SerializedName("sex")
	val sex: String? = null,

	@SerializedName("hideBodyInfo")
	val hideBodyInfo: Int? = null,

	@SerializedName("weight")
	val weight: Int? = null,

	@SerializedName("appleToken")
	val appleToken: String? = null,

	@SerializedName("photo")
	val photo: String? = null,

	@SerializedName("buildId")
	val buildId: String? = null,

	@SerializedName("googleToken")
	val googleToken: String? = null,

	@SerializedName("firstName")
	val firstName: String? = null,

	@SerializedName("districtId")
	val districtId: String? = null,

	@SerializedName("healthState")
	val healthState: String? = null,

	@SerializedName("phone")
	val phone: String? = null,

	@SerializedName("regionId")
	val regionId: String? = null,

	@SerializedName("profileFilling")
	val profileFilling: Int? = null,

	@SerializedName("userClub")
	val userClub: Any? = null,

	@SerializedName("lastActiveCoordinates")
	val lastActiveCoordinates: String? = null,

	@SerializedName("position")
	val position: String? = null,

	@SerializedName("isCoordinatorReport")
	val isCoordinatorReport: Int? = null,

	@SerializedName("age")
	val age: Int? = null,

	@SerializedName("isTrainer")
	val isTrainer: Int? = null,

	@SerializedName("facebookIdUser")
	val facebookIdUser: Any? = null
)
