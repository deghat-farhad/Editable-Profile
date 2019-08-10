package com.farhad.sparkeditableprofile.mapper

import com.farhad.sparkeditableprofile.testUtils.FakeSingleChoices
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SingleChoiceAnswerItemMapperTest {

    @Test
    fun mapListHashMapToPresentation() {
        val singleChoiceAnswers = FakeSingleChoices().generateFakeSingleChoiceAnswerListMap(10, 8)

        val singleChoiceAnswerItems = SingleChoiceAnswerItemMapper().mapListHashMapToPresentation(singleChoiceAnswers)

        for (question in singleChoiceAnswers.keys) {
            assertTrue(singleChoiceAnswerItems.keys.indexOf(question) >= 0)
            singleChoiceAnswers[question]?.let {
                for (cnt in (0 until it.size)) {
                    assertEquals(
                        singleChoiceAnswers[question]?.get(cnt)?.id,
                        singleChoiceAnswerItems[question]?.get(cnt)?.id
                    )

                    assertEquals(
                        singleChoiceAnswers[question]?.get(cnt)?.name,
                        singleChoiceAnswerItems[question]?.get(cnt)?.name
                    )
                }
            }
        }
    }

    @Test
    fun mapToPresentation(){
        val size = 100
        val singleChoiceAnswers = FakeSingleChoices().generateFakeSingleChoiceAnswerMap(size)

        val singleChoiceAnswerItems = SingleChoiceAnswerItemMapper().mapToPresentation(singleChoiceAnswers)

        assertEquals(singleChoiceAnswers.size, singleChoiceAnswerItems.size)

        for(question in singleChoiceAnswers.keys){
            assertEquals(singleChoiceAnswers[question]?.id, singleChoiceAnswerItems[question]?.id)
            assertEquals(singleChoiceAnswers[question]?.name, singleChoiceAnswerItems[question]?.name)
        }
    }

    @Test
    fun mapToDomain(){
        val size = 100
        val singleChoiceAnswerItems = FakeSingleChoices().generateFakeSingleChoiceAnswerItemMap(size)

        val singleChoiceAnswers = SingleChoiceAnswerItemMapper().mapToDomain(singleChoiceAnswerItems)

        assertEquals(singleChoiceAnswerItems.size, singleChoiceAnswers.size)

        for(question in singleChoiceAnswers.keys){
            assertEquals(singleChoiceAnswerItems[question]?.id, singleChoiceAnswers[question]?.id)
            assertEquals(singleChoiceAnswerItems[question]?.name, singleChoiceAnswers[question]?.name)
        }
    }
}