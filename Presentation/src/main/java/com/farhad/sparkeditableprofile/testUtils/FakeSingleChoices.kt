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
            val question = randomString()
            val answerList = mutableListOf<SingleChoiceAnswerItem>()
            for (answerCntr in (1..answerCnt)) {
                answerList.add(
                    SingleChoiceAnswerItem(randomString(), randomString())
                )
            }
            output[question] = answerList
        }

        return output
    }

    fun generateFakesingleChoiceAnswerMap(questionCnt: Int, answerCnt: Int): HashMap<String, List<SingleChoiceAnswer>>{
        val output = HashMap<String, List<SingleChoiceAnswer>>()

        for (questionCntr in (1 .. questionCnt)){
            val question = randomString()
            val answerList = mutableListOf<SingleChoiceAnswer>()
            for (answerCntr in (1 .. answerCnt)){
                answerList.add(
                    SingleChoiceAnswer(randomString(), randomString())
                )
            }
            output[question] = answerList
        }

        return output
    }

    private fun randomString(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val length = Random.nextInt(1, 30)
        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}