package com.ilya4.kickerandroid.presentation.di.module.activity

import com.ilya4.kickerandroid.presentation.presenter.ALoginPresenter
import com.ilya4.kickerandroid.presentation.view.view.ALoginMvpView
import dagger.Module
import dagger.Provides
import io.reactivex.processors.BehaviorProcessor

@Module
class LoginModule {

    @Provides
    fun provideALoginPresenter(view: ALoginMvpView,
                               behaviorProcessor: BehaviorProcessor<Boolean>): ALoginPresenter =
        ALoginPresenter(view, behaviorProcessor)
}