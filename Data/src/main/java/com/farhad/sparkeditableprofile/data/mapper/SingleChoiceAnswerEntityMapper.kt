package com.farhad.sparkeditableprofile.data.mapper

import com.farhad.sparkeditableprofile.data.entity.SingleChoiceAnswerEntity
import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer

class SingleChoiceAnswerEntityMapper {
    fun mapToData(singleChoiceAnswer: SingleChoiceAnswer) =
        SingleChoiceAnswerEntity(singleChoiceAnswer.id, singleChoiceAnswer.name)

    fun mapToData(singleChoiceAnswersMap: HashMap<String, SingleChoiceAnswer>)
            : Map<String, SingleChoiceAnswerEntity>{
        val outPut: MutableMap<String, SingleChoiceAnswerEntity> = mutableMapOf()

        for (key in singleChoiceAnswersMap.keys){
            val singleChoiceAnswer = singleChoiceAnswersMap[key]
            singleChoiceAnswer?.let { outPut[key] = mapToData(singleChoiceAnswer) }
        }
        return outPut
    }
}
