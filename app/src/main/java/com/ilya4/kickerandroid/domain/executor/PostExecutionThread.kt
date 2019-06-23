package com.ilya4.kickerandroid.domain.executor

import io.reactivex.Scheduler

interface PostExecutionThread {

    fun getScheluder() : Scheduler
}