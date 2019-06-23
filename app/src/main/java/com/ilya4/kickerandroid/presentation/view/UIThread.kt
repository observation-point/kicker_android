package com.ilya4.kickerandroid.presentation.view

import com.ilya4.kickerandroid.domain.executor.PostExecutionThread
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class UIThread @Inject constructor(): PostExecutionThread {
    override fun getScheluder(): Scheduler = AndroidSchedulers.mainThread()
}