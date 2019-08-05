package com.farhad.sparkeditableprofile.data.mapper

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
}