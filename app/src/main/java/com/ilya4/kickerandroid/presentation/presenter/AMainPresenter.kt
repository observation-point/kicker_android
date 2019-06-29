package com.ilya4.kickerandroid.presentation.presenter

import com.ilya4.kickerandroid.presentation.view.activity.base.BaseActivity
import com.ilya4.kickerandroid.presentation.view.view.AMainMvpView
import io.reactivex.processors.BehaviorProcessor

class AMainPresenter(view: AMainMvpView,
                     behaviorProcessor: BehaviorProcessor<Boolean>): BasePresenter<AMainMvpView>(view, behaviorProcessor) {

    override fun init(): Boolean {
        return false
    }

    override fun bindEvents(activity: BaseActivity) {

    }
}