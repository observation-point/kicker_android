package com.ilya4.kickerandroid.presentation.presenter

import com.ilya4.kickerandroid.presentation.view.activity.base.BaseActivity
import com.ilya4.kickerandroid.presentation.view.view.ALoginMvpView
import io.reactivex.processors.BehaviorProcessor

class ALoginPresenter(view: ALoginMvpView,
                      behaviorProcessor: BehaviorProcessor<Boolean>) : BasePresenter<ALoginMvpView>(view, behaviorProcessor) {


    override fun init(): Boolean {
       return false
    }

    override fun bindEvents(activity: BaseActivity) {

    }
}