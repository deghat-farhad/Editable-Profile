package com.farhad.sparkeditableprofile.updateProfile.view


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.view.View
import android.widget.DatePicker
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.rule.ActivityTestRule
import com.farhad.sparkeditableprofile.R
import com.farhad.sparkeditableprofile.di.ViewModelFactory
import com.farhad.sparkeditableprofile.model.SingleChoiceAnswerItem
import com.farhad.sparkeditableprofile.testUtils.ActFragTest
import com.farhad.sparkeditableprofile.testUtils.FakeSingleChoices
import com.farhad.sparkeditableprofile.testUtils.RandomString
import com.farhad.sparkeditableprofile.updateProfile.viewModel.UpdateProfileViewModel
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.random.Random
import kotlin.random.nextInt


@RunWith(AndroidJUnit4::class)
class FragUpdateProfileTest {

    @Rule
    @JvmField
    var testActivityRule = ActivityTestRule(ActFragTest::class.java, true, true)
    private var fragment = FragUpdateProfile()
    private val liveSingleChoiceAnswersMap = MutableLiveData<HashMap<String, List<SingleChoiceAnswerItem>>>()
    private val liveLocationList = MutableLiveData<List<String?>>()
    private val liveBirthday = MutableLiveData<String>()
    private val liveProfilePicture = MutableLiveData<Bitmap>()


    @Mock
    lateinit var updateProfileViewModel: UpdateProfileViewModel

    @Mock
    lateinit var viewModelFactory: ViewModelFactory

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        fragment.viewModelFactory = viewModelFactory
        testActivityRule.activity.setFragment(fragment)

        Mockito.`when`(viewModelFactory.create(UpdateProfileViewModel::class.java)).thenReturn(updateProfileViewModel)

        Mockito.`when`(updateProfileViewModel.questionSingleChoices).thenReturn(liveSingleChoiceAnswersMap)
        Mockito.`when`(updateProfileViewModel.questionLocationsStrings).thenReturn(liveLocationList)
        Mockito.`when`(updateProfileViewModel.birthday).thenReturn(liveBirthday)
        Mockito.`when`(updateProfileViewModel.profilePicture).thenReturn(liveProfilePicture)
    }

    @Test
    fun showSingleChoiceQuestionsTest() {
        val answersMap = FakeSingleChoices().generateFakeSingleChoiceAnswerItemMap(10, 8)

        runOnUiThread {
            liveSingleChoiceAnswersMap.value = answersMap
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

    @Test
    fun addLocationsToAutoCompleteEditTextTest() {
        val locationList = (1..100).map { RandomString().get() }
        runOnUiThread {
            liveLocationList.value = locationList
        }
        val selectedIndex = Random.nextInt(0 until locationList.size)
        val selectedLocationText = locationList[selectedIndex]

        onView(withId(R.id.autoCompleteTxtViewLocation))
            .perform(typeText(selectedLocationText.substring(0, selectedLocationText.length / 2)))
        onView(withText(selectedLocationText))
            .inRoot(withDecorView(not(`is`(testActivityRule.activity.window.decorView))))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withId(R.id.autoCompleteTxtViewLocation)).check(matches(withText(selectedLocationText)))
    }

    @Test
    fun displayDatePickerTest() {
        val year = Random.nextInt(1900..2100)
        val month = Random.nextInt(1..12)
        val day = Random.nextInt(1..30)

        Mockito.spy(updateProfileViewModel)

        onView(withId(R.id.txtInputEdtTxtBirthday)).perform(click())
        onView(withClassName(equalTo(DatePicker::class.java.name))).perform(PickerActions.setDate(year, month, day))
        onView(withText("OK")).perform(click())
        Mockito.verify(updateProfileViewModel).setNewBirthday(year, month - 1, day)
    }

    @Test
    fun setBirthdayEditTextsTextTest(){
        val birthday = RandomString().get()
        runOnUiThread {
            liveBirthday.value = birthday
        }

        onView(withId(R.id.txtInputEdtTxtBirthday)).check(matches(withText(birthday)))
    }

    @Test
    fun setProfilePictureTest(){
        onView(withId(R.id.imgViewEditProfilePic)).check(matches(hasDrawable()))
        onView(withId(R.id.imgViewEditProfilePic)).check(matches(DrawableMatcher(R.drawable.ic_profile)))

        val profilePicture = BitmapFactory.decodeResource (testActivityRule.activity.resources, android.R.drawable.ic_dialog_alert)
        runOnUiThread {
            liveProfilePicture.value = profilePicture
        }

        onView(withId(R.id.imgViewEditProfilePic)).check(matches(hasDrawable()))
        onView(withId(R.id.imgViewEditProfilePic)).check(matches(not(DrawableMatcher(R.drawable.ic_profile))))
    }

    private fun hasDrawable(): BoundedMatcher<View, ImageView> {
        return object : BoundedMatcher<View, ImageView>(ImageView::class.java) {
            override fun describeTo(description: org.hamcrest.Description?) {
                description?.appendText("has drawable")
            }

            public override fun matchesSafely(imageView: ImageView): Boolean {
                return imageView.drawable != null
            }
        }
    }
}

