package com.ilya4.kickerandroid.presentation.app

import com.google.gson.Gson
import com.ilya4.kickerandroid.data.io.model.AsyncData

import com.ilya4.kickerandroid.data.io.model.RestApi
import com.ilya4.kickerandroid.data.repository.SettingsDataSource
import com.ilya4.kickerandroid.domain.entity.GameStateResponse
import com.ilya4.kickerandroid.domain.entity.RestError
import timber.log.Timber
import java.io.IOException

class RestManager(val restApi: RestApi, val gson: Gson, val settingsDataSource : SettingsDataSource) {

    fun getGameState(asyncData: AsyncData<GameStateResponse>) {
        try {
            val response = restApi.getGameState().execute()
            if (response.isSuccessful && response.body() != null) {
                Timber.d("getGameState is success")
                asyncData.onSuccess((response.body() as GameStateResponse))
            } else {
                Timber.d(response.message())
                asyncData.onError(RestError(response.code().toString(), response.message()))
            }
        } catch (e: IOException) {
            Timber.e(e)
            asyncData.onFailure(e)
        }
    }
}