package com.ilya4.kickerandroid.presentation.app

import com.google.gson.Gson

import com.ilya4.kickerandroid.data.io.model.RestApi
import com.ilya4.kickerandroid.data.repository.SettingsDataSource

class RestManager(val restApi: RestApi, val gson: Gson, val settingsDataSource : SettingsDataSource) {

}