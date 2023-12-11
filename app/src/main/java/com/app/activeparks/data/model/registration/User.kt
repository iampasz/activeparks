package com.app.activeparks.data.model.registration

import com.google.gson.annotations.SerializedName

data class User(

	@SerializedName("birthday")
	val birthday: String? = null,

	@SerializedName("is_phone_verified")
	val isPhoneVerified: Boolean? = null,

	@SerializedName("last_login_at")
	val lastLoginAt: String? = null,

	@SerializedName("regions")
	val regions: List<String>? = null,

	@SerializedName("city")
	val city: String? = null,

	@SerializedName("hide_body_info")
	val hideBodyInfo: Int? = null,

	@SerializedName("about_me")
	val aboutMe: String? = null,

	@SerializedName("organisation_id")
	val organisationId: String? = null,

	@SerializedName("createdAt")
	val createdAt: String? = null,

	@SerializedName("password")
	val password: String? = null,

	@SerializedName("last_active_coordinates")
	val lastActiveCoordinates: String? = null,

	@SerializedName("updated_at")
	val updatedAt: String? = null,

	@SerializedName("role_id")
	val roleId: String? = null,

	@SerializedName("nickname")
	val nickname: String? = null,

	@SerializedName("id")
	val id: String,

	@SerializedName("refresh_token_issued_at")
	val refreshTokenIssuedAt: String? = null,

	@SerializedName("first_name")
	val firstName: String? = null,

	@SerializedName("sub_role")
	val subRole: Boolean? = null,

	@SerializedName("email")
	val email: String? = null,

	@SerializedName("height")
	val height: Int? = null,

	@SerializedName("health_state")
	val healthState: String? = null,

	@SerializedName("is_active")
	val isActive: Boolean? = null,

	@SerializedName("sex")
	val sex: String? = null,

	@SerializedName("region_id")
	val regionId: String? = null,

	@SerializedName("last_name")
	val lastName: String? = null,

	@SerializedName("weight")
	val weight: Int? = null,

	@SerializedName("photo")
	val photo: String? = null,

	@SerializedName("deleted_at")
	val deletedAt: String? = null,

	@SerializedName("is_trainer")
	val isTrainer: Int? = null,

	@SerializedName("refresh_token")
	val refreshToken: String? = null,

	@SerializedName("build_id")
	val buildId: String? = null,

	@SerializedName("phone")
	val phone: String? = null,

	@SerializedName("second_name")
	val secondName: String? = null,

	@SerializedName("district_id")
	val districtId: String? = null,

	@SerializedName("position")
	val position: String? = null
)