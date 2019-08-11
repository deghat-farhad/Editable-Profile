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

    fun mapToDomain(singleChoiceAnswerEntity: SingleChoiceAnswerEntity) =
        SingleChoiceAnswer(singleChoiceAnswerEntity.id, singleChoiceAnswerEntity.name)

    fun mapListToDomain(singleChoiceAnswersListMap: Map<String, List<SingleChoiceAnswerEntity>>): HashMap<String, List<SingleChoiceAnswer>> {
        val outPut = HashMap<String, List<SingleChoiceAnswer>>()

        for (key in singleChoiceAnswersListMap.keys) {
            val singleChoiceAnswerEntityList = singleChoiceAnswersListMap[key]
            singleChoiceAnswerEntityList?.let { singleChoiceAnswerEntities ->
                val singleChoiceAnswerList = singleChoiceAnswerEntities.map { mapToDomain(it) }
                outPut[key] = singleChoiceAnswerList
            }
        }
        return outPut
    }

    fun mapToDomain(singleChoiceAnswerEntitiesMap: Map<String, SingleChoiceAnswerEntity>)
            : HashMap<String, SingleChoiceAnswer> {
        val outPut = HashMap<String, SingleChoiceAnswer>()

        for (key in singleChoiceAnswerEntitiesMap.keys) {
            val singleChoiceAnswerEntity = singleChoiceAnswerEntitiesMap[key]
            singleChoiceAnswerEntity?.let { outPut[key] = mapToDomain(it) }
        }

        return outPut
    }
}
