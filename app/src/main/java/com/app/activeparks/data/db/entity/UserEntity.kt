package com.app.activeparks.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by O.Dziuba on 28.11.2023.
 */

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "keyId")
    val id: String,
    @ColumnInfo(name = "birthday")
    val birthday: String? = null,
    @ColumnInfo(name = "is_phone_verified")
    val isPhoneVerified: Boolean? = null,
    @ColumnInfo(name = "regions")
    val regions: List<String>? = null,
    @ColumnInfo(name = "city")
    val city: String? = null,
    @ColumnInfo(name = "hide_body_info")
    val hideBodyInfo: Int? = null,
    @ColumnInfo(name = "about_me")
    val aboutMe: String? = null,
    @ColumnInfo(name = "createdAt")
    val createdAt: String? = null,
    @ColumnInfo(name = "last_active_coordinates")
    val lastActiveCoordinates: String? = null,
    @ColumnInfo(name = "updated_at")
    val updatedAt: String? = null,
    @ColumnInfo(name = "role_id")
    val roleId: String? = null,
    @ColumnInfo(name = "nickname")
    val nickname: String? = null,
    @ColumnInfo(name = "first_name")
    val firstName: String? = null,
    @ColumnInfo(name = "sub_role")
    val subRole: Boolean? = null,
    @ColumnInfo(name = "email")
    val email: String? = null,
    @ColumnInfo(name = "height")
    val height: Int? = null,
    @ColumnInfo(name = "health_state")
    val healthState: String? = null,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean? = null,
    @ColumnInfo(name = "sex")
    val sex: String? = null,
    @ColumnInfo(name = "region_id")
    val regionId: String? = null,
    @ColumnInfo(name = "last_name")
    val lastName: String? = null,
    @ColumnInfo(name = "weight")
    val weight: Int? = null,
    @ColumnInfo(name = "photo")
    val photo: String? = null,
    @SerializedName("imageBackground")
    val imageBackground: String? = null,
    @ColumnInfo(name = "is_trainer")
    val isTrainer: Int? = null,
    @ColumnInfo(name = "build_id")
    val buildId: String? = null,
    @ColumnInfo(name = "phone")
    val phone: String? = null,
    @ColumnInfo(name = "second_name")
    val secondName: String? = null,
    @ColumnInfo(name = "district_id")
    val districtId: String? = null,
    @ColumnInfo(name = "position")
    val position: String? = null
)
