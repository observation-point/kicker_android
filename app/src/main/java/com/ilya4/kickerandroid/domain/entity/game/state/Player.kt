package com.ilya4.kickerandroid.domain.entity.game.state

import com.google.gson.annotations.SerializedName

data class Player(
    @SerializedName("role") val role: Role,
    @SerializedName("team") val team: Team,
    @SerializedName("user") val user: User,
    @SerializedName("winRate") val winRate: Float
)