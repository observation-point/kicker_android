package com.ilya4.kickerandroid.presentation.di.module.view.activity

import com.ilya4.kickerandroid.presentation.view.activity.LoginActivity
import com.ilya4.kickerandroid.presentation.view.view.ALoginMvpView
import dagger.Binds
import dagger.Module

@Module
abstract class LoginViewModule {

    @Binds
    abstract fun provideALoginMvpView(activity: LoginActivity): ALoginMvpView
}