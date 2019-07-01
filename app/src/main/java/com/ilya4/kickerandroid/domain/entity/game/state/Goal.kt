package com.ilya4.kickerandroid.domain.entity.game.state

import com.google.gson.annotations.SerializedName
import java.util.*

data class Goal(
    @SerializedName("id") val id: String,
    @SerializedName("team") val team: Team,
    @SerializedName("time") val time: Date
)