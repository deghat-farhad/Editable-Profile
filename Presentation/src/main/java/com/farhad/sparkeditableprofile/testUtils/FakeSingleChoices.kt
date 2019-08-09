package com.farhad.sparkeditableprofile.testUtils

import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer
import com.farhad.sparkeditableprofile.model.SingleChoiceAnswerItem
import kotlin.random.Random

class FakeSingleChoices {

    fun generateFakeSingleChoiceAnswerItemMap(
        questionCnt: Int,
        answerCnt: Int
    ): HashMap<String, List<SingleChoiceAnswerItem>> {
        val output = HashMap<String, List<SingleChoiceAnswerItem>>()

        for (questionCntr in (1..questionCnt)) {
            val question = RandomString().get()
            val answerList = mutableListOf<SingleChoiceAnswerItem>()
            for (answerCntr in (1..answerCnt)) {
                answerList.add(
                    SingleChoiceAnswerItem(RandomString().get(), RandomString().get())
                )
            }
            output[question] = answerList
        }

        return output
    }

    fun generateFakesingleChoiceAnswerMap(questionCnt: Int, answerCnt: Int): HashMap<String, List<SingleChoiceAnswer>>{
        val output = HashMap<String, List<SingleChoiceAnswer>>()

        for (questionCntr in (1 .. questionCnt)){
            val question = RandomString().get()
            val answerList = mutableListOf<SingleChoiceAnswer>()
            for (answerCntr in (1 .. answerCnt)){
                answerList.add(
                    SingleChoiceAnswer(RandomString().get(), RandomString().get())
                )
            }
            output[question] = answerList
        }

        return output
    }
}