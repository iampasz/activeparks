package com.app.activeparks.data.model.news

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ItemNews(
    @SerializedName("id")
    @Expose
    val id: String,

    @SerializedName("statusId")
    @Expose
    val statusId: String,

    @SerializedName("clubId")
    @Expose
    val clubId: String,

    @SerializedName("title")
    @Expose
    val title: String,

    @SerializedName("body")
    @Expose
    val body: String,

    @SerializedName("publishedAt")
    @Expose
    val publishedAt: String,

    @SerializedName("createdAt")
    @Expose
    val createdAt: String,

    @SerializedName("imageUrl")
    @Expose
    val imageUrl: String,

    @SerializedName("photo")
    @Expose
    val photo: String,

    @SerializedName("photos")
    @Expose
    val photos: List<String>,

    @SerializedName("bodStyles")
    @Expose
    val bodStyles: String,

    @SerializedName("createdById")
    @Expose
    val createdById: String,

    @SerializedName("updatedAt")
    @Expose
    val updatedAt: String,

    @SerializedName("deletedAt")
    @Expose
    val deletedAt: String,

    @SerializedName("createdBy")
    @Expose
    val createdBy: CreatedBy
)