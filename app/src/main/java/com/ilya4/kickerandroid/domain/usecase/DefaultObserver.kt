package com.ilya4.kickerandroid.domain.usecase


import io.reactivex.observers.DisposableObserver

open class DefaultObserver<T> : DisposableObserver<T>() {

    override fun onComplete() {
    }

    override fun onNext(result: T) {
    }

    override fun onError(e: Throwable) {
    }
}