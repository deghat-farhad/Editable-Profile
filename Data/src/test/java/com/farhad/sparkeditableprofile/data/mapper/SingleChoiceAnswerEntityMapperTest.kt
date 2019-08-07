package com.farhad.sparkeditableprofile.data.mapper

import com.farhad.sparkeditableprofile.data.entity.SingleChoiceAnswerEntity
import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer
import net.bytebuddy.utility.RandomString
import org.junit.Assert.assertEquals
import org.junit.Test

class SingleChoiceAnswerEntityMapperTest {

    @Test
    fun mapToData() {
        val singleChoiceAnswer = generateSingleChoicesAnswer()


        val singleChoiceAnswerEntityMapper = SingleChoiceAnswerEntityMapper()
        val singleChoiceAnswerEntity = singleChoiceAnswerEntityMapper.mapToData(singleChoiceAnswer)

        assertEquals(singleChoiceAnswer.id, singleChoiceAnswerEntity.id)
        assertEquals(singleChoiceAnswer.name,singleChoiceAnswerEntity.name)
    }

    @Test
    fun mapAnswersHashMapToData(){
        val answerHashMap = generateSingleChoicesAnswerMap(10)
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
        val singleChoiceAnswerEntity = generateSingleChoicesAnswerEntity()

        val singleChoiceAnswerEntityMapper = SingleChoiceAnswerEntityMapper()
        val singleChoiceAnswer = singleChoiceAnswerEntityMapper.mapToDomain(singleChoiceAnswerEntity)

        assertEquals(singleChoiceAnswerEntity.name, singleChoiceAnswer.name)
        assertEquals(singleChoiceAnswerEntity.id, singleChoiceAnswer.id)
    }

    @Test
    fun mapListToDomain(){
        val singleChoiceAnswerEntityMap
                = generateFakeSingleChoiceAnswerEntityMap(10, 10)

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

    private fun generateSingleChoicesAnswer() = SingleChoiceAnswer(RandomString.make(), RandomString.make())

    private fun generateSingleChoicesAnswerMap(count: Int): HashMap<String, SingleChoiceAnswer> {
        val output = HashMap<String, SingleChoiceAnswer>()

        (1..count).map {
            val singleChoiceAnswer = generateSingleChoicesAnswer()
            val key = RandomString.make()
            output[key] = singleChoiceAnswer
        }
        return output
    }

    private fun generateSingleChoicesAnswerEntity() = SingleChoiceAnswerEntity(RandomString.make(), RandomString.make())

    private fun generateFakeSingleChoiceAnswerEntityMap(questionCnt: Int, answerCnt: Int): HashMap<String, List<SingleChoiceAnswerEntity>>{
        val output = HashMap<String, List<SingleChoiceAnswerEntity>>()

        for (questionCntr in (1 .. questionCnt)){
            val question = RandomString.make()
            val answerList = mutableListOf<SingleChoiceAnswerEntity>()
            for (answerCntr in (1 .. answerCnt)){
                answerList.add(
                    SingleChoiceAnswerEntity(RandomString.make(), RandomString.make())
                )
            }
            output[question] = answerList
        }

        return output
    }

}