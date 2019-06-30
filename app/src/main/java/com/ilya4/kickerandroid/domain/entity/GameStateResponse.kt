package com.ilya4.kickerandroid.domain.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class GameStateResponse(
    @SerializedName("id") val id: String,
    @SerializedName("players") val players: List<Player>,
    @SerializedName("status") val status: String,
    @SerializedName("startGame") val startGame: Date
)