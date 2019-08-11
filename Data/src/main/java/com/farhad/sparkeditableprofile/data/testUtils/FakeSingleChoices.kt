package com.farhad.sparkeditableprofile.data.testUtils

import com.farhad.sparkeditableprofile.data.entity.SingleChoiceAnswerEntity
import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer

class FakeSingleChoices {

    fun generateFakeSingleChoiceAnswerEntityListMap(
        questionCnt: Int,
        answerCnt: Int
    ): HashMap<String, List<SingleChoiceAnswerEntity>> {
        val output = HashMap<String, List<SingleChoiceAnswerEntity>>()

        for (questionCntr in (1..questionCnt)) {
            val question = RandomString().get()
            val answerList = mutableListOf<SingleChoiceAnswerEntity>()
            for (answerCntr in (1..answerCnt)) {
                answerList.add(
                    SingleChoiceAnswerEntity(RandomString().get(), RandomString().get())
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

        for (questionCntr in (1..questionCnt)) {
            val question = RandomString().get()
            val answerList = mutableListOf<SingleChoiceAnswer>()
            for (answerCntr in (1..answerCnt)) {
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

    fun generateFakeSingleChoiceAnswerEntityMap(size: Int): HashMap<String, SingleChoiceAnswerEntity> {
        val output = HashMap<String, SingleChoiceAnswerEntity>()
        for (cnt in (1..size)) {
            output[RandomString().get()] = SingleChoiceAnswerEntity(RandomString().get(), RandomString().get())
        }
        return output
    }
}