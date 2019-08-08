package com.farhad.sparkeditableprofile.updateProfile.view


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.rule.ActivityTestRule
import com.farhad.sparkeditableprofile.R
import com.farhad.sparkeditableprofile.testUtils.ActFragTest
import com.farhad.sparkeditableprofile.updateProfile.model.SingleChoiceAnswerItem
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class FragUpdateProfileTest {

    @Rule
    @JvmField
    var testActivityRule = ActivityTestRule(ActFragTest::class.java, true, true)
    var fragment = FragUpdateProfile()
    val answersMap = generateFakeSingleChoiceAnswerItemMap(10, 8)


    @Before
    fun setUp() {
        testActivityRule.activity.setFragment(fragment)
    }

    @Test
    fun test() {
        runOnUiThread {
            fragment.addSingleChoiceQuestions(answersMap)
        }
        onView(withId(R.id.singleChoiceItemsContainer)).check(matches(hasChildCount(10)))
        for (key in answersMap.keys) {
            val singleChoiceAnswerInteraction = onView(withHint(key))
            singleChoiceAnswerInteraction
                .perform(scrollTo())
                .check(matches(isDisplayed()))
                .perform(click())
            answersMap[key]?.let {
                for (answer in it) {
                    onView(withText(answer.name))
                        .perform(scrollTo())
                        .check(matches(isDisplayed()))
                }
                val selectIndex = Random.nextInt(it.size)
                onView(withText(it[selectIndex].name))
                    .perform(click())
                onView(withText("OK")).perform(click())
                singleChoiceAnswerInteraction
                    .check(matches(withText(it[selectIndex].name)))
            }
        }
    }

    private fun generateFakeSingleChoiceAnswerItemMap(
        questionCnt: Int,
        answerCnt: Int
    ): HashMap<String, List<SingleChoiceAnswerItem>> {
        val output = HashMap<String, List<SingleChoiceAnswerItem>>()

        for (questionCntr in (1..questionCnt)) {
            val question = randomString()
            val answerList = mutableListOf<SingleChoiceAnswerItem>()
            for (answerCntr in (1..answerCnt)) {
                answerList.add(
                    SingleChoiceAnswerItem(randomString(), randomString())
                )
            }
            output[question] = answerList
        }

        return output
    }

    private fun randomString(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val length = Random.nextInt(1, 30)
        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

}