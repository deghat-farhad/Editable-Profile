package com.farhad.sparkeditableprofile.updateProfile.view


import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.rule.ActivityTestRule
import com.farhad.sparkeditableprofile.R
import com.farhad.sparkeditableprofile.di.ViewModelFactory
import com.farhad.sparkeditableprofile.testUtils.ActFragTest
import com.farhad.sparkeditableprofile.model.SingleChoiceAnswerItem
import com.farhad.sparkeditableprofile.testUtils.FakeSingleChoices
import com.farhad.sparkeditableprofile.updateProfile.viewModel.UpdateProfileViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class FragUpdateProfileTest {

    @Rule
    @JvmField
    var testActivityRule = ActivityTestRule(ActFragTest::class.java, true, true)
    private var fragment = FragUpdateProfile()
    private val answersMap = FakeSingleChoices().generateFakeSingleChoiceAnswerItemMap(10, 8)

    @Mock
    lateinit var updateProfileViewModel: UpdateProfileViewModel

    @Mock
    lateinit var viewModelFactory: ViewModelFactory




    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        fragment.viewModelFactory = viewModelFactory
        testActivityRule.activity.setFragment(fragment)
    }

    @Test
    fun test() {
        Mockito.`when`(viewModelFactory.create(UpdateProfileViewModel::class.java)).thenReturn(updateProfileViewModel)
        val singleChoiceAnswersMap = MutableLiveData<HashMap<String, List<SingleChoiceAnswerItem>>>()
        Mockito.`when`(updateProfileViewModel.questionSingleChoices).thenReturn(singleChoiceAnswersMap)

        runOnUiThread {
            singleChoiceAnswersMap.value = answersMap
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
}