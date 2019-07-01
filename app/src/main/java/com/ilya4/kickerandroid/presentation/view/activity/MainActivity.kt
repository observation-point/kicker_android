package com.ilya4.kickerandroid.presentation.view.activity

import android.os.Bundle
import com.ilya4.kickerandroid.R
import com.ilya4.kickerandroid.presentation.presenter.AMainPresenter
import com.ilya4.kickerandroid.presentation.view.activity.base.BaseActivity
import com.ilya4.kickerandroid.presentation.view.view.AMainMvpView
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : BaseActivity(), AMainMvpView {

    @Inject
    lateinit var presenter: AMainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.init()
    }
}