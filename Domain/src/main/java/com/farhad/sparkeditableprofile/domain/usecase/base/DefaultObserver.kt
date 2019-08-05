package com.farhad.sparkeditableprofile.domain.usecase.base

import io.reactivex.observers.DisposableObserver

abstract class DefaultObserver<T>: DisposableObserver<T>() {
    override fun onComplete() {

    }

    override fun onNext(it: T) {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
    }
}