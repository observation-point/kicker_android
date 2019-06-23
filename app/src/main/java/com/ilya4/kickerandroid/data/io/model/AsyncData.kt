package com.ilya4.kickerandroid.data.io.model

import com.ilya4.kickerandroid.domain.entity.RestError

interface AsyncData<T> {
    fun onSuccess(data : T)
    fun onError(restError: RestError)
    fun onFailure(t: Throwable)
}