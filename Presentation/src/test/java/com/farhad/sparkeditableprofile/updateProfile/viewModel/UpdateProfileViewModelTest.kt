package com.farhad.sparkeditableprofile.updateProfile.viewModel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.farhad.sparkeditableprofile.domain.model.*
import com.farhad.sparkeditableprofile.domain.usecase.base.DefaultObserver
import com.farhad.sparkeditableprofile.domain.usecase.getLocations.GetLocations
import com.farhad.sparkeditableprofile.domain.usecase.getSingleChoiceAnswers.GetSingleChoiceAnswers
import com.farhad.sparkeditableprofile.domain.usecase.registerProfile.RegisterProfile
import com.farhad.sparkeditableprofile.domain.usecase.registerProfile.RegisterProfileParams
import com.farhad.sparkeditableprofile.domain.usecase.uploadProfilePicture.UploadProfilePicture
import com.farhad.sparkeditableprofile.domain.usecase.uploadProfilePicture.UploadProfilePictureParams
import com.farhad.sparkeditableprofile.mapper.LocationItemMapper
import com.farhad.sparkeditableprofile.mapper.ProfileItemMapper
import com.farhad.sparkeditableprofile.mapper.SingleChoiceAnswerItemMapper
import com.farhad.sparkeditableprofile.testUtils.FakeLocations
import com.farhad.sparkeditableprofile.testUtils.FakeProfile
import com.farhad.sparkeditableprofile.testUtils.FakeSingleChoices
import com.farhad.sparkeditableprofile.testUtils.RandomString
import junit.framework.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*
import org.mockito.ArgumentCaptor.forClass
import org.mockito.Mockito.mock
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.collections.HashMap
import kotlin.random.Random
import kotlin.random.nextInt






class UpdateProfileViewModelTest {
    @Mock
    lateinit var getSingleChoiceAnswers: GetSingleChoiceAnswers

    @Mock
    lateinit var getLocations: GetLocations

    @Mock
    lateinit var singleChoiceAnswerItemMapper: SingleChoiceAnswerItemMapper

    @Mock
    lateinit var locationItemMapper: LocationItemMapper

    @Mock
    lateinit var registerProfile: RegisterProfile

    @Mock
    lateinit var profileItemMapper: ProfileItemMapper

    @Mock
    lateinit var uploadProfilePicture: UploadProfilePicture

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testGetSingleChoices() {

        val singleChoices = FakeSingleChoices().generateFakeSingleChoiceAnswerListMap(10, 8)
        val singleChoiceItems = FakeSingleChoices().generateFakeSingleChoiceAnswerItemListMap(10, 8)

        Mockito.`when`(
            getSingleChoiceAnswers.execute(
                any(),
                any()
            )
        ).thenAnswer {
            var observer: DefaultObserver<HashMap<String, List<SingleChoiceAnswer>>> =
                object : DefaultObserver<HashMap<String, List<SingleChoiceAnswer>>>() {}
            if (it.arguments != null && it.arguments.isNotEmpty() && it.arguments[0] != null) {
                observer = it.arguments[0] as DefaultObserver<HashMap<String, List<SingleChoiceAnswer>>>
                observer.onNext(singleChoices)
            }
            return@thenAnswer observer
        }

        Mockito.`when`(singleChoiceAnswerItemMapper.mapListHashMapToPresentation(singleChoices))
            .thenReturn(singleChoiceItems)
        val updateProfileViewModel = updateProfileViewModel()

        assertEquals(singleChoiceItems, updateProfileViewModel.questionSingleChoices.value)
    }

    @Test
    fun getLocations(){
        val locations = FakeLocations().generateLocationList(100)
        val locationItems = FakeLocations().generateLocationItemList(100)

        Mockito.`when`(
            getLocations.execute(
                any(),
                any()
            )
        ).thenAnswer{
            var observer: DefaultObserver<List<Location>> =
                object : DefaultObserver<List<Location>>() {}
            if (it.arguments != null && it.arguments.isNotEmpty() && it.arguments[0] != null) {
                observer = it.arguments[0] as DefaultObserver<List<Location>>
                observer.onNext(locations)
            }
            return@thenAnswer observer
        }
        Mockito.`when`(locationItemMapper.mapToPresentation(locations))
            .thenReturn(locationItems)

        val updateProfileViewModel = updateProfileViewModel()

        assertEquals(locationItems.map { it.city }, updateProfileViewModel.questionLocationsStrings.value)
    }

    @Test
    fun setNewBirthdayTest() {
        val monthsName = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val year = Random.nextInt(1900..2100)
        val month = Random.nextInt(0..11)
        val day = Random.nextInt(1..30)

        val updateProfileViewModel = updateProfileViewModel()

        updateProfileViewModel.setNewBirthday(year, month, day)

        assertEquals(updateProfileViewModel.birthday.value, "$day ${monthsName[month]} $year")
    }

    @Test(expected = IOException::class)
    fun setProfilePictureTest(){
        val bmp = mock(Bitmap::class.java)
        val path = "cacheDir"

        val updateProfileViewModel = updateProfileViewModel()

        updateProfileViewModel.setProfilePicture(bmp, path)

        assertEquals(updateProfileViewModel.profilePicture.value, bmp)
    }

    @Test
    fun submit(){
        val locationItems = FakeLocations().generateLocationItemList(100).toMutableList()
        val singleChoiceItems = FakeSingleChoices().generateFakeSingleChoiceAnswerItemListMap(10, 8)
        val profile = FakeProfile(locationItems, singleChoiceItems).getProfile()

        val answers = HashMap<String, String>()
        for (question in profile.answers.keys){
            profile.answers[question]?.let {
                answers[question] = it.name!!
            }
        }

        Mockito.`when`(profileItemMapper.mapToDomain(any()))
            .thenReturn(profile)

        val updateProfileViewModel = updateProfileViewModel()

        updateProfileViewModel.newBirthDay = profile.birthday
        updateProfileViewModel.questionLocations = locationItems
        updateProfileViewModel.questionSingleChoices.value = singleChoiceItems
        updateProfileViewModel.questionLocationsStrings.value = locationItems.map { it.city }

        updateProfileViewModel.submit(
            profile.displayName.toString(),
            profile.realName.toString(),
            profile.occupation.toString(),
            profile.aboutMe.toString(),
            profile.location?.city.toString(),
            profile.height!!,
            answers)

        val captor:ArgumentCaptor<RegisterProfileParams> =  forClass(RegisterProfileParams::class.java)

        Mockito.verify(registerProfile).execute(any(), capture(captor))

        assertEquals(profile.displayName, captor.value.profile.displayName)
        assertEquals(profile.realName, captor.value.profile.realName)
        assertEquals(profile.occupation, captor.value.profile.occupation)
        assertEquals(profile.aboutMe, captor.value.profile.aboutMe)
        assertEquals(profile.height, captor.value.profile.height)

        assertEquals(profile.location?.lat, captor.value.profile.location?.lat)
        assertEquals(profile.location?.lon, captor.value.profile.location?.lon)
        assertEquals(profile.location?.city, captor.value.profile.location?.city)

        for(question in profile.answers.keys){
            assertEquals(profile.answers[question]?.id, captor.value.profile.answers[question]?.id)
            assertEquals(profile.answers[question]?.name, captor.value.profile.answers[question]?.name)
        }
    }

    @Test
    fun uploadPictureTest(){
        val locationItems = FakeLocations().generateLocationItemList(100).toMutableList()
        val singleChoiceItems = FakeSingleChoices().generateFakeSingleChoiceAnswerItemListMap(10, 8)
        val fakeProfile = FakeProfile(locationItems, singleChoiceItems)
        val profileItem = fakeProfile.getProfileItem()
        val profile = fakeProfile.getProfile()

        val profilePicture = File(RandomString().get())

        Mockito.`when`(profileItemMapper.mapToDomain(any()))
            .thenReturn(profile)
        val updateProfileViewModel = updateProfileViewModel()

        updateProfileViewModel.uploadPicture(profileItem, profilePicture)

        val captor:ArgumentCaptor<UploadProfilePictureParams> =  forClass(UploadProfilePictureParams::class.java)

        Mockito.verify(uploadProfilePicture).execute(any(), capture(captor))

        assertEquals(profile, captor.value.profile)
        assertEquals(profilePicture, captor.value.picture)
    }

    private fun updateProfileViewModel() = UpdateProfileViewModel(
        singleChoiceAnswerItemMapper,
        locationItemMapper,
        getSingleChoiceAnswers,
        getLocations,
        registerProfile,
        profileItemMapper,
        uploadProfilePicture
    )

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    /**
     * Returns ArgumentCaptor.capture() as nullable type to avoid java.lang.IllegalStateException
     * when null is returned.
     */
    fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
}