package com.farhad.sparkeditableprofile.domain.testUtils

import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer

class FakeSingleChoice {

    fun generateFakeSingleChoiceAnswerMap(size: Int): HashMap<String, SingleChoiceAnswer> {
        val output = HashMap<String, SingleChoiceAnswer>()
        for (cnt in (1..size)) {
            output[RandomString().get()] = SingleChoiceAnswer(RandomString().get(), RandomString().get())
        }
        return output
    }
}