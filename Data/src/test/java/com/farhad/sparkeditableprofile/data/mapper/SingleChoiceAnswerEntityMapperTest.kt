package com.farhad.sparkeditableprofile.data.mapper

import com.farhad.sparkeditableprofile.data.entity.SingleChoiceAnswerEntity
import com.farhad.sparkeditableprofile.data.testUtils.FakeSingleChoices
import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer
import net.bytebuddy.utility.RandomString
import org.junit.Assert.assertEquals
import org.junit.Test

class SingleChoiceAnswerEntityMapperTest {

    @Test
    fun mapToData() {
        val singleChoiceAnswer = SingleChoiceAnswer(RandomString.make(), RandomString.make())


        val singleChoiceAnswerEntityMapper = SingleChoiceAnswerEntityMapper()
        val singleChoiceAnswerEntity = singleChoiceAnswerEntityMapper.mapToData(singleChoiceAnswer)

        assertEquals(singleChoiceAnswer.id, singleChoiceAnswerEntity.id)
        assertEquals(singleChoiceAnswer.name,singleChoiceAnswerEntity.name)
    }

    @Test
    fun mapAnswersHashMapToData(){
        val answerHashMap = FakeSingleChoices().generateFakeSingleChoiceAnswerMap(10)
        val singleChoiceAnswerEntityMapper = SingleChoiceAnswerEntityMapper()
        val singleChoiceAnswerEntityHashMap
                = singleChoiceAnswerEntityMapper.mapToData(answerHashMap)

        assertEquals(singleChoiceAnswerEntityHashMap.size, 10)
        for (key in singleChoiceAnswerEntityHashMap.keys){
            assertEquals(singleChoiceAnswerEntityHashMap[key]?.id, answerHashMap[key]?.id)
            assertEquals(singleChoiceAnswerEntityHashMap[key]?.name, answerHashMap[key]?.name)
        }
    }

    @Test
    fun mapToDomain(){
        val singleChoiceAnswerEntity = SingleChoiceAnswerEntity(RandomString.make(), RandomString.make())

        val singleChoiceAnswerEntityMapper = SingleChoiceAnswerEntityMapper()
        val singleChoiceAnswer = singleChoiceAnswerEntityMapper.mapToDomain(singleChoiceAnswerEntity)

        assertEquals(singleChoiceAnswerEntity.name, singleChoiceAnswer.name)
        assertEquals(singleChoiceAnswerEntity.id, singleChoiceAnswer.id)
    }

    @Test
    fun mapListToDomain(){
        val singleChoiceAnswerEntityMap
                = FakeSingleChoices().generateFakeSingleChoiceAnswerEntityListMap(10, 10)

        val singleChoiceAnswerEntityMapper = SingleChoiceAnswerEntityMapper()
        val singleChoiceAnswerMap
                = singleChoiceAnswerEntityMapper.mapListToDomain(singleChoiceAnswerEntityMap)
        for (key in singleChoiceAnswerMap.keys){
            singleChoiceAnswerMap[key]?.let {
                for (cntr in (0 until it.size)) {
                    assertEquals(singleChoiceAnswerEntityMap[key]?.get(cntr)?.id,
                        singleChoiceAnswerMap[key]?.get(cntr)?.id)

                    assertEquals(singleChoiceAnswerEntityMap[key]?.get(cntr)?.name,
                        singleChoiceAnswerMap[key]?.get(cntr)?.name)
                }
            }
        }
    }

    @Test
    fun mapToDomainTest(){
        val fakeSingleChoiceEntityMap = FakeSingleChoices().generateFakeSingleChoiceAnswerEntityMap(10)

        val singleChoiceAnswerEntityMapper = SingleChoiceAnswerEntityMapper()
        val mappedAnswers = singleChoiceAnswerEntityMapper.mapToDomain(fakeSingleChoiceEntityMap)

        assertEquals(fakeSingleChoiceEntityMap.size, mappedAnswers.size)
        for (key in fakeSingleChoiceEntityMap.keys){
            assertEquals(fakeSingleChoiceEntityMap[key]?.id, mappedAnswers[key]?.id)
            assertEquals(fakeSingleChoiceEntityMap[key]?.name, mappedAnswers[key]?.name)
        }
    }
}