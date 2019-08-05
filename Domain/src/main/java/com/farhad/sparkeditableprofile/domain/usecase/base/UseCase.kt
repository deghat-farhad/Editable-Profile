package com.farhad.sparkeditableprofile.domain.usecase.base

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

abstract class UseCase<T, Params>(private val executorThread: Scheduler,
                                  private val uiThread: Scheduler
) {
    abstract fun buildUseCaseObservable(params: Params): Observable<T>

    open fun execute(observer: DisposableObserver<T>, params: Params): DisposableObserver<T>? {
        val observable = buildUseCaseObservable(params)
            .subscribeOn(executorThread)
            .observeOn(uiThread)
        return (observable.subscribeWith(observer))
    }
}