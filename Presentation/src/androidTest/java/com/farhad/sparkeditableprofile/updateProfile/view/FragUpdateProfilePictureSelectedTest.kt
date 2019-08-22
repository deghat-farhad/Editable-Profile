package com.farhad.sparkeditableprofile.updateProfile.view

import android.app.Activity
import android.app.Instrumentation
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasType
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.farhad.editableprofile.utils.SingleLiveEvent
import com.farhad.sparkeditableprofile.R
import com.farhad.sparkeditableprofile.di.ViewModelFactory
import com.farhad.sparkeditableprofile.model.SingleChoiceAnswerItem
import com.farhad.sparkeditableprofile.testUtils.ActFragTest
import com.farhad.sparkeditableprofile.updateProfile.viewModel.UpdateProfileViewModel
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


@RunWith(AndroidJUnit4::class)
class FragUpdateProfilePictureSelectedTest {
    @JvmField
    @Rule
    val testActivityRule = ActivityTestRule(ActFragTest::class.java)

    private var fragment = FragUpdateProfile()
    private val liveSingleChoiceAnswersMap = MutableLiveData<HashMap<String, List<SingleChoiceAnswerItem>>>()
    private val liveLocationList = MutableLiveData<List<String?>>()
    private val liveBirthday = MutableLiveData<String>()
    private val liveProfilePicture = MutableLiveData<Bitmap>()

    private val displayName: MutableLiveData<String> = MutableLiveData<String>()
    private val realName: MutableLiveData<String> = MutableLiveData<String>()
    private val occupation: MutableLiveData<String> = MutableLiveData<String>()
    private val height: MutableLiveData<String> = MutableLiveData<String>()
    private val aboutMe: MutableLiveData<String> = MutableLiveData<String>()
    private val location: MutableLiveData<String> = MutableLiveData<String>()
    private val answers: MutableLiveData<java.util.HashMap<String, String>> =
        MutableLiveData<java.util.HashMap<String, String>>()

    private val profileRegistered: SingleLiveEvent<String> = SingleLiveEvent<String>()
    private val profileUpdated: SingleLiveEvent<Unit> = SingleLiveEvent<Unit>()
    private val registerIsInProgress: SingleLiveEvent<Unit> = SingleLiveEvent<Unit>()

    private val birthDayValidation: SingleLiveEvent<String> = SingleLiveEvent<String>()
    private val displayNameValidation: SingleLiveEvent<String> = SingleLiveEvent<String>()
    private val realNameValidation: SingleLiveEvent<String> = SingleLiveEvent<String>()
    private val aboutMeValidation: SingleLiveEvent<String> = SingleLiveEvent<String>()
    private val occupationValidation: SingleLiveEvent<String> = SingleLiveEvent<String>()
    private val heightValidation: SingleLiveEvent<String> = SingleLiveEvent<String>()
    private val locationValidation: SingleLiveEvent<String> = SingleLiveEvent<String>()

    @Mock
    lateinit var updateProfileViewModel: UpdateProfileViewModel

    @Mock
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var mActivityResult: Instrumentation.ActivityResult

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

        Mockito.`when`(updateProfileViewModel.displayName).thenReturn(displayName)
        Mockito.`when`(updateProfileViewModel.realName).thenReturn(realName)
        Mockito.`when`(updateProfileViewModel.height).thenReturn(height)
        Mockito.`when`(updateProfileViewModel.occupation).thenReturn(occupation)
        Mockito.`when`(updateProfileViewModel.location).thenReturn(location)
        Mockito.`when`(updateProfileViewModel.aboutMe).thenReturn(aboutMe)
        Mockito.`when`(updateProfileViewModel.answers).thenReturn(answers)

        Mockito.`when`(updateProfileViewModel.profileRegistered).thenReturn(profileRegistered)
        Mockito.`when`(updateProfileViewModel.profileUpdated).thenReturn(profileUpdated)
        Mockito.`when`(updateProfileViewModel.registerIsInProgress).thenReturn(registerIsInProgress)

        Mockito.`when`(updateProfileViewModel.displayNameValidation).thenReturn(displayNameValidation)
        Mockito.`when`(updateProfileViewModel.realNameValidation).thenReturn(realNameValidation)
        Mockito.`when`(updateProfileViewModel.aboutMeValidation).thenReturn(aboutMeValidation)
        Mockito.`when`(updateProfileViewModel.birthDayValidation).thenReturn(birthDayValidation)
        Mockito.`when`(updateProfileViewModel.heightValidation).thenReturn(heightValidation)
        Mockito.`when`(updateProfileViewModel.locationValidation).thenReturn(locationValidation)
        Mockito.`when`(updateProfileViewModel.occupationValidation).thenReturn(occupationValidation)

        mActivityResult = getImageResult()
    }

    @Test
    fun testPick() {
        Intents.init()
        val expectedIntent = allOf(
            hasAction(Intent.ACTION_PICK),
            hasType("image/*")
        )

        intending(expectedIntent).respondWith(mActivityResult)
        onView(withId(R.id.btnAddProfilePic)).perform(click())
        intended(expectedIntent)
        Intents.release()

        Mockito.verify(updateProfileViewModel).setProfilePicture(any<Bitmap>(), any())
    }

    private fun  getImageResult():Instrumentation.ActivityResult {
        val resultData = Intent()
        val resources = InstrumentationRegistry.getInstrumentation().targetContext.resources
        val imageUri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources
                .getResourcePackageName(
                    R.mipmap.ic_launcher
                ) + '/'.toString() + resources.getResourceTypeName(
                R.mipmap.ic_launcher
            ) + '/'.toString() + resources.getResourceEntryName(
                R.mipmap.ic_launcher
            )
        )
        resultData.data = imageUri
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}