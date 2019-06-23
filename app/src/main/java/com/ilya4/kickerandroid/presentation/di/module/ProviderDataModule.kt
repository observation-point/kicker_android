package com.ilya4.kickerandroid.presentation.di.module



import com.ilya4.kickerandroid.data.repository.SettingsDataSource
import com.ilya4.kickerandroid.data.repository.local.SettingsLocalDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ProviderDataModule {

    @Provides
    @Singleton
    fun provideSettingsDataSource(settingsLocalDataSource: SettingsLocalDataSource) : SettingsDataSource = settingsLocalDataSource
}