package com.farhad.sparkeditableprofile.testUtils

import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer
import com.farhad.sparkeditableprofile.model.SingleChoiceAnswerItem

class FakeSingleChoices {

    fun generateFakeSingleChoiceAnswerItemListMap(
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

    fun generateFakeSingleChoiceAnswerListMap(
        questionCnt: Int,
        answerCnt: Int
    ): HashMap<String, List<SingleChoiceAnswer>> {
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

    fun generateFakeSingleChoiceAnswerMap(size: Int): HashMap<String, SingleChoiceAnswer> {
        val output = HashMap<String, SingleChoiceAnswer>()
        for (cnt in (1..size)) {
            output[RandomString().get()] = SingleChoiceAnswer(RandomString().get(), RandomString().get())
        }
        return output
    }

    fun generateFakeSingleChoiceAnswerItemMap(size: Int): HashMap<String, SingleChoiceAnswerItem> {
        val output = HashMap<String, SingleChoiceAnswerItem>()
        for (cnt in (1..size)) {
            output[RandomString().get()] = SingleChoiceAnswerItem(RandomString().get(), RandomString().get())
        }
        return output
    }
}