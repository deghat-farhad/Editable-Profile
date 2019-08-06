package com.farhad.sparkeditableprofile.domain.usecase.getLocations

import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.domain.repository.QuestionRepository
import com.farhad.sparkeditableprofile.domain.usecase.base.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler


class GetLocations(
    executorThread: Scheduler,
    uiThread: Scheduler,
    private val questionsRepository: QuestionRepository
) : UseCase<List<Location>, Unit>(executorThread, uiThread) {
    override fun buildUseCaseObservable(params: Unit): Observable<List<Location>> {
        return questionsRepository.getLocations()
    }
}