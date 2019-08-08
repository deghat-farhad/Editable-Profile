package com.farhad.sparkeditableprofile.mapper

import com.farhad.sparkeditableprofile.testUtils.FakeSingleChoices
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SingleChoiceAnswerItemMapperTest {

    @Test
    fun mapListHashMapToPresentation() {
        val singleChoiceAnswers = FakeSingleChoices().generateFakesingleChoiceAnswerMap(10, 8)

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
}