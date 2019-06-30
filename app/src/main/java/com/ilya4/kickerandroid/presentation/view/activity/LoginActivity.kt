package com.ilya4.kickerandroid.presentation.view.activity

import android.os.Bundle
import com.ilya4.kickerandroid.R
import com.ilya4.kickerandroid.presentation.presenter.ALoginPresenter
import com.ilya4.kickerandroid.presentation.view.activity.base.BaseActivity
import com.ilya4.kickerandroid.presentation.view.view.ALoginMvpView
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity: BaseActivity(), ALoginMvpView{

    @Inject
    lateinit var presenter: ALoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        addOnClickListeners()
    }

    private fun addOnClickListeners() {
        completeBtn.setOnClickListener {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        startActivity(MainActivity::class.java, true)
    }
}