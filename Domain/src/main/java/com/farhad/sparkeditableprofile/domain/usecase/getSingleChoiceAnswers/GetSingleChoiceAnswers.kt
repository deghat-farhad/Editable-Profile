package com.farhad.sparkeditableprofile.domain.usecase.getSingleChoiceAnswers

import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer
import com.farhad.sparkeditableprofile.domain.repository.QuestionRepository
import com.farhad.sparkeditableprofile.domain.usecase.base.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

class GetSingleChoiceAnswers(
    executorThread: Scheduler,
    uiThread: Scheduler,
    private val questionsRepository: QuestionRepository
) : UseCase<HashMap<String, List<SingleChoiceAnswer>>, Unit>(executorThread, uiThread) {
    override fun buildUseCaseObservable(params: Unit): Observable<HashMap<String, List<SingleChoiceAnswer>>> {
        return questionsRepository.getSingleChoiceAnswers()
    }
}