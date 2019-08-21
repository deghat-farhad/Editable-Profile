package com.farhad.sparkeditableprofile.viewProfile.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.farhad.sparkeditableprofile.domain.model.Profile
import com.farhad.sparkeditableprofile.domain.usecase.base.DefaultObserver
import com.farhad.sparkeditableprofile.domain.usecase.getProfile.GetProfile
import com.farhad.sparkeditableprofile.domain.usecase.getProfile.GetProfileParams
import com.farhad.sparkeditableprofile.mapper.ProfileItemMapper
import com.farhad.sparkeditableprofile.testUtils.FakeLocations
import com.farhad.sparkeditableprofile.testUtils.FakeProfile
import com.farhad.sparkeditableprofile.testUtils.FakeSingleChoices
import com.farhad.sparkeditableprofile.testUtils.RandomString
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

class ViewProfileViewModelTest {

    @Mock
    lateinit var getProfile: GetProfile

    @Mock
    lateinit var profileItemMapper: ProfileItemMapper

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getProfile() {
        val locationItems = FakeLocations().generateLocationItemList(100).toMutableList()
        val singleChoiceItems = FakeSingleChoices().generateFakeSingleChoiceAnswerItemListMap(10, 8)
        val fakeProfile = FakeProfile(locationItems, singleChoiceItems)
        val profile = fakeProfile.getProfile()
        val profileItem = fakeProfile.getProfileItem()
        val id = RandomString().get()
        val monthsName = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val calendar = Calendar.getInstance()
        calendar.time = profileItem.birthday

        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)


        Mockito.`when`(profileItemMapper.mapToPresentation(any()))
            .thenReturn(profileItem)

        val viewProfileViewModel = ViewProfileViewModel(getProfile, profileItemMapper)

        viewProfileViewModel.getProfile(id)

        val getProfileParamsCaptor: ArgumentCaptor<GetProfileParams> =
            ArgumentCaptor.forClass(GetProfileParams::class.java)
        val getProfileObserverCaptor: ArgumentCaptor<DefaultObserver<Profile>> =
            ArgumentCaptor.forClass(DefaultObserver::class.java) as ArgumentCaptor<DefaultObserver<Profile>>

        Mockito.verify(getProfile).execute(capture(getProfileObserverCaptor), capture(getProfileParamsCaptor))
        getProfileObserverCaptor.value.onNext(profile)

        assertEquals(id, getProfileParamsCaptor.value.userId)
        assertEquals(viewProfileViewModel.aboutMe.value, profileItem.aboutMe)
        assertEquals(viewProfileViewModel.displayName.value, profileItem.displayName)
        assertEquals(viewProfileViewModel.birthday.value, "$day ${monthsName[month]} $year")
        assertEquals(viewProfileViewModel.height.value, profileItem.height.toString())
        assertEquals(viewProfileViewModel.location.value, profileItem.location?.city)

        for (question in profileItem.answers.keys) {
            assertEquals(profileItem.answers.keys.size, viewProfileViewModel.answers.value?.keys?.size)
            viewProfileViewModel.answers.value?.keys?.let {
                assertTrue(it.indexOf(question) >= 0)
                assertEquals(viewProfileViewModel.answers.value?.get(question), profileItem.answers[question]?.name)
            }
        }
    }

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