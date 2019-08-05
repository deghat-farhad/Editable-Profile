package com.farhad.sparkeditableprofile.domain.usecase.base

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.observers.DisposableObserver
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito

class UseCaseTest {

    private val subscribeOnScheduler = Mockito.mock(Scheduler::class.java)
    private val observeOnScheduler = Mockito.mock(Scheduler::class.java)
    private val observer = Mockito.mock(DisposableObserver::class.java) as DisposableObserver<Unit>
    private val observable = Mockito.mock(Observable::class.java) as Observable<Unit>

    @Before
    fun setup() {
        Mockito.`when`(observable.subscribeOn(any(Scheduler::class.java))).thenReturn(observable)
        Mockito.`when`(observable.observeOn(any(Scheduler::class.java))).thenReturn(observable)
    }

    @Test
    fun checkSchedulers() {
        val useCase = object : UseCase<Unit, Unit>(subscribeOnScheduler, observeOnScheduler) {
            override fun buildUseCaseObservable(params: Unit) = observable
        }

        useCase.execute(observer, Unit)

        Mockito.verify(observable).subscribeOn(subscribeOnScheduler)
        Mockito.verify(observable).observeOn(observeOnScheduler)
    }

    @Test
    fun checkSubscriber() {
        val useCase = object : UseCase<Unit, Unit>(subscribeOnScheduler, observeOnScheduler) {
            override fun buildUseCaseObservable(params: Unit) = observable
        }

        useCase.execute(observer, Unit)
        Mockito.verify(observable).subscribeWith(observer)
    }

    @Test
    fun checkOutPut() {
        val useCase = object : UseCase<Unit, Unit>(subscribeOnScheduler, observeOnScheduler) {
            override fun buildUseCaseObservable(params: Unit) = observable
        }

        val disposableObserver = useCase.execute(observer, Unit)
        assertEquals(disposableObserver, observable.subscribeWith(observer))
    }
}