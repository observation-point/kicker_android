package com.ilya4.kickerandroid.presentation.di.module.view.activity

import com.ilya4.kickerandroid.presentation.view.activity.MainActivity
import com.ilya4.kickerandroid.presentation.view.view.AMainMvpView
import dagger.Binds
import dagger.Module

@Module
abstract class MainViewModule {

    @Binds
    abstract fun provideAMainMvpView(activity: MainActivity) : AMainMvpView
}