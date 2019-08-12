package com.farhad.sparkeditableprofile.viewProfile.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.farhad.editableprofile.utils.SingleLiveEvent
import com.farhad.sparkeditableprofile.R
import com.farhad.sparkeditableprofile.di.ViewModelFactory
import com.farhad.sparkeditableprofile.model.ProfileItem
import com.farhad.sparkeditableprofile.testUtils.ActFragTest
import com.farhad.sparkeditableprofile.testUtils.RandomString
import com.farhad.sparkeditableprofile.viewProfile.viewModel.ViewProfileViewModel
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*


@RunWith(AndroidJUnit4::class)
class FragViewProfileTest {

    @Rule
    @JvmField
    var testActivityRule = ActivityTestRule(ActFragTest::class.java, true, true)
    private var fragment = FragViewProfile()

    @Mock
    lateinit var viewProfileViewModel: ViewProfileViewModel

    @Mock
    lateinit var viewModelFactory: ViewModelFactory

    val displayName: MutableLiveData<String> = MutableLiveData<String>()
    val profilePicture: MutableLiveData<Bitmap> = MutableLiveData<Bitmap>()
    val birthday: MutableLiveData<String> = MutableLiveData<String>()
    val height: MutableLiveData<String> = MutableLiveData<String>()
    val aboutMe: MutableLiveData<String> = MutableLiveData<String>()
    val location: MutableLiveData<String> = MutableLiveData<String>()
    val answers: MutableLiveData<HashMap<String, String>> = MutableLiveData<HashMap<String, String>>()
    val navigateToEditProfile: SingleLiveEvent<ProfileItem> = SingleLiveEvent<ProfileItem>()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        fragment.viewModelFactory = viewModelFactory
        testActivityRule.activity.setFragment(fragment)

        Mockito.`when`(viewProfileViewModel.displayName).thenReturn(displayName)
        Mockito.`when`(viewProfileViewModel.profilePicture).thenReturn(profilePicture)
        Mockito.`when`(viewProfileViewModel.birthday).thenReturn(birthday)
        Mockito.`when`(viewProfileViewModel.height).thenReturn(height)
        Mockito.`when`(viewProfileViewModel.aboutMe).thenReturn(aboutMe)
        Mockito.`when`(viewProfileViewModel.location).thenReturn(location)
        Mockito.`when`(viewProfileViewModel.answers).thenReturn(answers)
        Mockito.`when`(viewProfileViewModel.navigateToEditProfile).thenReturn(navigateToEditProfile)

        Mockito.`when`(viewModelFactory.create(ViewProfileViewModel::class.java)).thenReturn(viewProfileViewModel)
    }

    @Test
    fun testObserves() {
        val displayName = RandomString().get()
        val birthday = RandomString().get()
        val height = RandomString().get()
        val aboutMe = RandomString().get()
        val location = RandomString().get()
        val answers = HashMap<String, String>()
        val bmp = BitmapFactory.decodeResource(
            InstrumentationRegistry.getInstrumentation().context.resources,
            android.R.drawable.ic_menu_gallery
        )
        for (cnt in (0..9)) {
            answers[RandomString().get()] = RandomString().get()
        }

        onView(withId(R.id.imgViwProfile)).check(matches(not(hasDrawable())))
        runOnUiThread {
            this.displayName.value = displayName
            this.birthday.value = birthday
            this.height.value = height
            this.aboutMe.value = aboutMe
            this.location.value = location
            this.profilePicture.value = bmp
            this.answers.value = answers
        }
        onView(withId(R.id.txtViwDisplayName)).check(matches(withText(displayName)))
        onView(withId(R.id.txtViwBirthday)).check(matches(withText(birthday)))
        onView(withId(R.id.txtViwHeight)).check(matches(withText(height)))
        onView(withId(R.id.txtViwAboutMe)).check(matches(withText(aboutMe)))
        onView(withId(R.id.txtViwLocation)).check(matches(withText(location)))
        onView(withId(R.id.imgViwProfile)).check(matches(hasDrawable()))
        for (question in answers.keys) {
            onView(withText(question)).check(matches(hasSibling(withText(answers[question]))))
        }
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