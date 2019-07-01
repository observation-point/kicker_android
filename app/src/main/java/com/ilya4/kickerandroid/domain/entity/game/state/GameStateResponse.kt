package com.ilya4.kickerandroid.domain.entity.game.state

import com.google.gson.annotations.SerializedName
import java.util.*

data class GameStateResponse(
    @SerializedName("id") val id: String,
    @SerializedName("players") val players: List<Player>,
    @SerializedName("status") val status: GameStatus,
    @SerializedName("startGame") val startGame: Date,
    @SerializedName("endGame") val endGame: Date,
    @SerializedName("goals") val goals: List<Goal>)