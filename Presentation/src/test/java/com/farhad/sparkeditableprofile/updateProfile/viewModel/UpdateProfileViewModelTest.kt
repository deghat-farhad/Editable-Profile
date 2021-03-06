package com.farhad.sparkeditableprofile.updateProfile.viewModel

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer
import com.farhad.sparkeditableprofile.domain.usecase.base.DefaultObserver
import com.farhad.sparkeditableprofile.domain.usecase.getLocations.GetLocations
import com.farhad.sparkeditableprofile.domain.usecase.getSingleChoiceAnswers.GetSingleChoiceAnswers
import com.farhad.sparkeditableprofile.domain.usecase.registerProfile.RegisterProfile
import com.farhad.sparkeditableprofile.domain.usecase.registerProfile.RegisterProfileParams
import com.farhad.sparkeditableprofile.domain.usecase.updateProfile.UpdateProfile
import com.farhad.sparkeditableprofile.domain.usecase.updateProfile.UpdateProfileParams
import com.farhad.sparkeditableprofile.domain.usecase.uploadProfilePicture.UploadProfilePicture
import com.farhad.sparkeditableprofile.domain.usecase.uploadProfilePicture.UploadProfilePictureParams
import com.farhad.sparkeditableprofile.mapper.LocationItemMapper
import com.farhad.sparkeditableprofile.mapper.ProfileItemMapper
import com.farhad.sparkeditableprofile.mapper.SingleChoiceAnswerItemMapper
import com.farhad.sparkeditableprofile.model.LocationItem
import com.farhad.sparkeditableprofile.model.ProfileItem
import com.farhad.sparkeditableprofile.testUtils.FakeLocations
import com.farhad.sparkeditableprofile.testUtils.FakeProfile
import com.farhad.sparkeditableprofile.testUtils.FakeSingleChoices
import com.farhad.sparkeditableprofile.testUtils.RandomString
import com.farhad.sparkeditableprofile.updateProfile.viewModel.validator.DateValidator
import com.farhad.sparkeditableprofile.updateProfile.viewModel.validator.LocationValidator
import com.farhad.sparkeditableprofile.updateProfile.viewModel.validator.TextValidator
import com.farhad.sparkeditableprofile.updateProfile.viewModel.validator.ValidationException
import com.farhad.sparkeditableprofile.utils.CropImage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.rules.TemporaryFolder
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentCaptor.forClass
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.io.File
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

    @Mock
    lateinit var profileItemDiff: ProfileItemDiff

    @Mock
    lateinit var updateProfile: UpdateProfile

    @Mock
    lateinit var cropImage:CropImage

    @Mock
    lateinit var textValidator: TextValidator

    @Mock
    lateinit var dateValidator: DateValidator

    @Mock
    lateinit var locationValidator: LocationValidator

    @get:Rule
    val expected = ExpectedException.none()


    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    var testFolder = TemporaryFolder()

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
        val updateProfileViewModel = updateProfileViewModel(null)

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

        val updateProfileViewModel = updateProfileViewModel(null)

        assertEquals(locationItems.map { it.city }, updateProfileViewModel.questionLocationsStrings.value)
    }

    @Test
    fun setNewBirthdayTest() {
        val monthsName = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val year = Random.nextInt(1900..2100)
        val month = Random.nextInt(0..11)
        val day = Random.nextInt(1..30)

        val updateProfileViewModel = updateProfileViewModel(null)

        updateProfileViewModel.setNewBirthday(year, month, day)

        assertEquals(updateProfileViewModel.birthday.value, "$day ${monthsName[month]} $year")
    }

    @Test
    fun setProfilePictureTest(){
        val bmp = mock(Bitmap::class.java)
        Mockito.`when`(cropImage.crop(any(), anyInt(), anyInt())).thenReturn(bmp)
        val path = testFolder.root.absolutePath

        val updateProfileViewModel = updateProfileViewModel(null)

        updateProfileViewModel.setProfilePicture(bmp, path)

        val file = File("$path/tmp.jpg")
        assertTrue(file.exists())
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

        val updateProfileViewModel = updateProfileViewModel(null)

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

        assertEquals("", updateProfileViewModel.occupationValidation.value)
        assertEquals("", updateProfileViewModel.locationValidation.value)
        assertEquals("", updateProfileViewModel.heightValidation.value)
        assertEquals("", updateProfileViewModel.aboutMeValidation.value)
        assertEquals("", updateProfileViewModel.realNameValidation.value)
        assertEquals("", updateProfileViewModel.birthDayValidation.value)
        assertEquals("", updateProfileViewModel.displayNameValidation.value)
    }

    @Test
    fun isItValidate() {
        val locationItems = FakeLocations().generateLocationItemList(100).toMutableList()
        val singleChoiceItems = FakeSingleChoices().generateFakeSingleChoiceAnswerItemListMap(10, 8)
        val profile = FakeProfile(locationItems, singleChoiceItems).getProfile()

        val answers = HashMap<String, String>()
        for (question in profile.answers.keys) {
            profile.answers[question]?.let {
                answers[question] = it.name!!
            }
        }

        val exceptionMessage = RandomString().get()

        Mockito.`when`(profileItemMapper.mapToDomain(any()))
            .thenReturn(profile)

        Mockito.`when`(locationValidator.validate(any<LocationItem>())).thenThrow(ValidationException(exceptionMessage))
        Mockito.`when`(textValidator.validate(anyString(), anyInt(), anyBoolean(), any()))
            .thenThrow(ValidationException(exceptionMessage))
        Mockito.`when`(dateValidator.confirmIsOlderThan(any(), anyInt()))
            .thenThrow(ValidationException(exceptionMessage))

        val updateProfileViewModel = updateProfileViewModel(null)

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
            answers
        )

        verifyZeroInteractions(registerProfile)

        assertEquals(exceptionMessage, updateProfileViewModel.occupationValidation.value)
        assertEquals(exceptionMessage, updateProfileViewModel.locationValidation.value)
        assertEquals(exceptionMessage, updateProfileViewModel.heightValidation.value)
        assertEquals(exceptionMessage, updateProfileViewModel.aboutMeValidation.value)
        assertEquals(exceptionMessage, updateProfileViewModel.realNameValidation.value)
        assertEquals(exceptionMessage, updateProfileViewModel.birthDayValidation.value)
        assertEquals(exceptionMessage, updateProfileViewModel.displayNameValidation.value)
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
        val updateProfileViewModel = updateProfileViewModel(null)

        updateProfileViewModel.uploadPicture(profileItem, profilePicture)

        val captor:ArgumentCaptor<UploadProfilePictureParams> =  forClass(UploadProfilePictureParams::class.java)

        Mockito.verify(uploadProfilePicture).execute(any(), capture(captor))

        assertEquals(profile, captor.value.profile)
        assertEquals(profilePicture, captor.value.picture)
    }

    @Test
    fun testWhenThereIsAProfileItem(){
        val locationItems = FakeLocations().generateLocationItemList(100).toMutableList()
        val singleChoiceItems = FakeSingleChoices().generateFakeSingleChoiceAnswerItemListMap(10, 8)
        val profileItem = FakeProfile(locationItems, singleChoiceItems).getProfileItem()

        val updateProfileViewModel = updateProfileViewModel(profileItem)
        updateProfileViewModel.setLiveAnswers(profileItem.answers)

        assertEquals(profileItem.displayName, updateProfileViewModel.displayName.value)
        assertEquals(profileItem.realName, updateProfileViewModel.realName.value)
        assertEquals(profileItem.occupation, updateProfileViewModel.occupation.value)
        assertEquals(profileItem.height.toString(), updateProfileViewModel.height.value)
        assertEquals(profileItem.aboutMe, updateProfileViewModel.aboutMe.value)
        assertEquals(profileItem.location?.city, updateProfileViewModel.location.value)
        for(question in profileItem.answers.keys){
            assertEquals(profileItem.answers[question]?.name, updateProfileViewModel.answers.value?.get(question))
        }
    }

    @Test
    fun updateProfile(){
        val locationItems = FakeLocations().generateLocationItemList(100).toMutableList()
        val singleChoiceItems = FakeSingleChoices().generateFakeSingleChoiceAnswerItemListMap(10, 8)
        val profile = FakeProfile(locationItems, singleChoiceItems).getProfile()
        val diffFakeProfile = FakeProfile(locationItems, singleChoiceItems)
        val diffProfile = diffFakeProfile.getProfile()
        val diffProfileItem = diffFakeProfile.getProfileItem()

        val answers = HashMap<String, String>()
        for (question in profile.answers.keys){
            profile.answers[question]?.let {
                answers[question] = it.name!!
            }
        }

        Mockito.`when`(profileItemMapper.mapToDomain(any()))
            .thenReturn(diffProfile)

        Mockito.`when`(profileItemDiff.getDiff(any(), any())).thenReturn(diffProfileItem)

        val updateProfileViewModel = updateProfileViewModel(null)

        updateProfileViewModel.newBirthDay = profile.birthday
        updateProfileViewModel.questionLocations = locationItems
        updateProfileViewModel.questionSingleChoices.value = singleChoiceItems
        updateProfileViewModel.questionLocationsStrings.value = locationItems.map { it.city }
        updateProfileViewModel.profileItem = FakeProfile(locationItems, singleChoiceItems).getProfileItem()

        updateProfileViewModel.submit(
            profile.displayName.toString(),
            profile.realName.toString(),
            profile.occupation.toString(),
            profile.aboutMe.toString(),
            profile.location?.city.toString(),
            profile.height!!,
            answers)

        val captor:ArgumentCaptor<UpdateProfileParams> =  forClass(UpdateProfileParams::class.java)

        Mockito.verify(updateProfile).execute(any(), capture(captor))

        assertEquals(diffProfile.displayName, captor.value.profile.displayName)
        assertEquals(diffProfile.realName, captor.value.profile.realName)
        assertEquals(diffProfile.occupation, captor.value.profile.occupation)
        assertEquals(diffProfile.aboutMe, captor.value.profile.aboutMe)
        assertEquals(diffProfile.height, captor.value.profile.height)

        assertEquals(diffProfile.location?.lat, captor.value.profile.location?.lat)
        assertEquals(diffProfile.location?.lon, captor.value.profile.location?.lon)
        assertEquals(diffProfile.location?.city, captor.value.profile.location?.city)

        for(question in diffProfile.answers.keys){
            assertEquals(diffProfile.answers[question]?.id, captor.value.profile.answers[question]?.id)
            assertEquals(diffProfile.answers[question]?.name, captor.value.profile.answers[question]?.name)
        }
    }

    private fun updateProfileViewModel(profileItem: ProfileItem?) = UpdateProfileViewModel(
        singleChoiceAnswerItemMapper,
        locationItemMapper,
        getSingleChoiceAnswers,
        getLocations,
        registerProfile,
        profileItemMapper,
        uploadProfilePicture,
        profileItem,
        updateProfile,
        profileItemDiff,
        cropImage,
        textValidator,
        dateValidator,
        locationValidator
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