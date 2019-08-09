package com.farhad.sparkeditableprofile.updateProfile.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer
import com.farhad.sparkeditableprofile.domain.usecase.base.DefaultObserver
import com.farhad.sparkeditableprofile.domain.usecase.getLocations.GetLocations
import com.farhad.sparkeditableprofile.domain.usecase.getSingleChoiceAnswers.GetSingleChoiceAnswers
import com.farhad.sparkeditableprofile.mapper.LocationItemMapper
import com.farhad.sparkeditableprofile.mapper.SingleChoiceAnswerItemMapper
import com.farhad.sparkeditableprofile.testUtils.FakeLocations
import com.farhad.sparkeditableprofile.testUtils.FakeSingleChoices
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
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

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testGetSingleChoices() {

        val singleChoices = FakeSingleChoices().generateFakesingleChoiceAnswerMap(10, 8)
        val singleChoiceItems = FakeSingleChoices().generateFakeSingleChoiceAnswerItemMap(10, 8)

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
        val updateProfileViewModel = UpdateProfileViewModel(
            singleChoiceAnswerItemMapper,
            locationItemMapper,
            getSingleChoiceAnswers,
            getLocations
        )

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

        val updateProfileViewModel = UpdateProfileViewModel(
            singleChoiceAnswerItemMapper,
            locationItemMapper,
            getSingleChoiceAnswers,
            getLocations
        )

        assertEquals(locationItems.map { it.city }, updateProfileViewModel.questionLocationsStrings.value)
    }

    @Test
    fun setNewBirthdayTest() {
        val monthsName = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val year = Random.nextInt(1900..2100)
        val month = Random.nextInt(0..11)
        val day = Random.nextInt(1..30)

        val updateProfileViewModel = UpdateProfileViewModel(
            singleChoiceAnswerItemMapper,
            locationItemMapper,
            getSingleChoiceAnswers,
            getLocations
        )

        updateProfileViewModel.setNewBirthday(year, month, day)

        assertEquals(updateProfileViewModel.birthday.value, "$day ${monthsName[month]} $year")
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}