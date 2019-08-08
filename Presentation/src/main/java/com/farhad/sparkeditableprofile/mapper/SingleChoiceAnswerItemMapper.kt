package com.farhad.sparkeditableprofile.mapper

import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer
import com.farhad.sparkeditableprofile.model.SingleChoiceAnswerItem
import javax.inject.Inject

open class SingleChoiceAnswerItemMapper @Inject constructor() {
    private fun mapToPresentation(singleChoiceAnswer: SingleChoiceAnswer) =
        SingleChoiceAnswerItem(singleChoiceAnswer.id, singleChoiceAnswer.name)

    open fun mapListHashMapToPresentation(singleChoiceAnswerMap: HashMap<String, List<SingleChoiceAnswer>>)
            : HashMap<String, List<SingleChoiceAnswerItem>> {
        val output = HashMap<String, List<SingleChoiceAnswerItem>>()

        for (singleChoiceAnswersKey in singleChoiceAnswerMap.keys) {
            singleChoiceAnswerMap[singleChoiceAnswersKey]?.let { answers ->
                output.put(singleChoiceAnswersKey, answers.map {
                    mapToPresentation(
                        it
                    )
                })
            }
        }
        return output
    }
}