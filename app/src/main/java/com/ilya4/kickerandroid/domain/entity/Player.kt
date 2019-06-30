package com.ilya4.kickerandroid.domain.entity

import com.google.gson.annotations.SerializedName

data class Player(
    @SerializedName("role") val role: String,
    @SerializedName("team") val team: String,
    @SerializedName("user") val user: User,
    @SerializedName("winRate") val winRate: Float
)