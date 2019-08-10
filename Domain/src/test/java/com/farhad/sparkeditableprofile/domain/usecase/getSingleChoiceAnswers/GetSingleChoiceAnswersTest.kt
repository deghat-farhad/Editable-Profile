package com.farhad.sparkeditableprofile.domain.usecase.getSingleChoiceAnswers

import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer
import com.farhad.sparkeditableprofile.domain.repository.QuestionRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import net.bytebuddy.utility.RandomString
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GetSingleChoiceAnswersTest{

    @Mock lateinit var scheduler: Scheduler
    @Mock lateinit var questionRepository: QuestionRepository

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun buildUseCaseObservable() {
        val answersMap = generateFakeLocationMap(10, 10)
        Mockito.`when`(questionRepository.getSingleChoiceAnswers()).thenReturn(Observable.just(answersMap))

        val getSingleChoiceAnswer = GetSingleChoiceAnswers(scheduler, scheduler, questionRepository)

        val observableSingleChoiceAnswers
                = getSingleChoiceAnswer.buildUseCaseObservable(Unit)

        observableSingleChoiceAnswers.test().assertValue {
            it == answersMap
        }.onComplete()
    }

    private fun generateFakeLocationMap(questionCnt: Int, answerCnt: Int): HashMap<String, List<SingleChoiceAnswer>>{
        val output = HashMap<String, List<SingleChoiceAnswer>>()

        for (questionCntr in (1 .. questionCnt)){
            val question = RandomString.make()
            val answerList = mutableListOf<SingleChoiceAnswer>()
            for (answerCntr in (1 .. answerCnt)){
                answerList.add(
                    SingleChoiceAnswer(RandomString.make(), RandomString.make())
                )
            }
            output[question] = answerList
        }

        return output
    }
}