package com.ilya4.kickerandroid.data.io.model

import com.ilya4.kickerandroid.domain.entity.GameStateResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers

interface RestApi {

    @Headers("Content-Type: application/json")
    @GET("api/game")
    fun getGameState() : Call<GameStateResponse>
}