package com.ilya4.kickerandroid.domain.entity

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: String,
    @SerializedName("login") val login: String,
    @SerializedName("fullname") val fullname: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("token") val token: String)