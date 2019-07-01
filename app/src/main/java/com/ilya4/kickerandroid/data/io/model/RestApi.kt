package com.ilya4.kickerandroid.data.io.model

import com.ilya4.kickerandroid.domain.entity.game.state.GameStateResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface RestApi {

    @Headers("Content-Type: application/json")
    @GET("game")
    fun getGameState() : Call<GameStateResponse>
}