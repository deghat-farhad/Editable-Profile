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

    fun mapToPresentation(singleChoiceAnserMap: HashMap<String, SingleChoiceAnswer>)
            : HashMap<String, SingleChoiceAnswerItem> {
        val outPut: HashMap<String, SingleChoiceAnswerItem> = hashMapOf()

        for (key in singleChoiceAnserMap.keys) {
            singleChoiceAnserMap[key]?.let {
                outPut[key] = mapToPresentation(it)
            }
        }

        return outPut
    }

    private fun mapToDomain(singleChoiceAnswerItem: SingleChoiceAnswerItem) =
        SingleChoiceAnswer(singleChoiceAnswerItem.id, singleChoiceAnswerItem.name)

    fun mapToDomain(singleChoiceAnwserItemMap: HashMap<String, SingleChoiceAnswerItem>)
            : HashMap<String, SingleChoiceAnswer> {
        val outPut: HashMap<String, SingleChoiceAnswer> = hashMapOf()

        for (key in singleChoiceAnwserItemMap.keys) {
            singleChoiceAnwserItemMap[key]?.let {
                outPut[key] = mapToDomain(it)
            }
        }

        return outPut
    }
}