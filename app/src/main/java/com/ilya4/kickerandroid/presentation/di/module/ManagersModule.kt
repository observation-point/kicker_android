package com.ilya4.kickerandroid.presentation.di.module


import com.google.gson.Gson
import com.ilya4.kickerandroid.data.io.model.RestApi
import com.ilya4.kickerandroid.data.repository.SettingsDataSource
import com.ilya4.kickerandroid.presentation.app.RestManager

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ManagersModule {

    @Provides
    @Singleton
    fun provideRestManager(restApi: RestApi, gson: Gson, settingsDataSource: SettingsDataSource) : RestManager {
        return RestManager(restApi, gson, settingsDataSource)
    }
}