package com.ilya4.kickerandroid.presentation.presenter

import com.ilya4.kickerandroid.domain.usecase.DefaultObserver
import com.ilya4.kickerandroid.domain.usecase.GetGameStateUseCase
import com.ilya4.kickerandroid.presentation.view.activity.base.BaseActivity
import com.ilya4.kickerandroid.presentation.view.view.AMainMvpView

import io.reactivex.processors.BehaviorProcessor

class AMainPresenter(view: AMainMvpView,
                     behaviorProcessor: BehaviorProcessor<Boolean>,
                     private val getGameStateUseCase: GetGameStateUseCase): BasePresenter<AMainMvpView>(view, behaviorProcessor) {

    override fun init(): Boolean {
        getGameState()
        return false
    }

    override fun bindEvents(activity: BaseActivity) {

    }

    private fun getGameState() {
        getGameStateUseCase.execute(GetGameStateObserver(), GetGameStateUseCase.Params.forGetGameState())
    }

    inner class GetGameStateObserver: DefaultObserver<GetGameStateUseCase.Result>() {
        override fun onNext(result: GetGameStateUseCase.Result) {
            if (result.errorMessage == null) {

            }
        }
    }
}