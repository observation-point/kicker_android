package com.ilya4.kickerandroid.presentation.di.module

import com.ilya4.kickerandroid.presentation.di.module.activity.LoginModule
import com.ilya4.kickerandroid.presentation.di.module.view.activity.LoginViewModule
import com.ilya4.kickerandroid.presentation.view.activity.LoginActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector(modules = [LoginViewModule::class, LoginModule::class])
    abstract fun bindLoginActivity() : LoginActivity
}
