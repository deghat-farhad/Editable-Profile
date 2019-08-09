package com.farhad.sparkeditableprofile.updateProfile.view

import android.app.Activity
import android.app.Instrumentation
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasType
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.farhad.sparkeditableprofile.R
import com.farhad.sparkeditableprofile.di.ViewModelFactory
import com.farhad.sparkeditableprofile.model.SingleChoiceAnswerItem
import com.farhad.sparkeditableprofile.testUtils.ActFragTest
import com.farhad.sparkeditableprofile.updateProfile.viewModel.UpdateProfileViewModel
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.io.File
import java.io.FileOutputStream


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

        savePickedImage()

        mActivityResult = getImageResult()
    }

    @Test
    fun testPick() {
        Intents.init()
        val expectedIntent = allOf(
            hasAction(Intent.ACTION_GET_CONTENT),
            hasType("image/*")
        )

        intending(expectedIntent).respondWith(mActivityResult)
        onView(withId(R.id.btnAddProfilePic)).perform(click())
        intended(expectedIntent)
        Intents.release()

        Mockito.verify(updateProfileViewModel).setProfilePicture(any())
    }

    private fun  getImageResult():Instrumentation.ActivityResult {
        val resultData = Intent()
        val dir = testActivityRule.activity.externalCacheDir
        dir?.let {
            val file = File(dir.path, "pickImageResult.jpeg")
            val uri = Uri.fromFile(file)
            resultData.data = uri
        }
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    private fun savePickedImage()
    {
        val bm = BitmapFactory.decodeResource (testActivityRule.activity.resources, android.R.drawable.ic_dialog_alert)
        assertTrue(bm != null)
        val dir = testActivityRule.activity.externalCacheDir
        dir?.let {
            val file = File(dir.path, "pickImageResult.jpeg")
            println(file.absolutePath)
            val outStream = FileOutputStream (file)
            bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
            outStream.flush()
            outStream.close()
        }
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}