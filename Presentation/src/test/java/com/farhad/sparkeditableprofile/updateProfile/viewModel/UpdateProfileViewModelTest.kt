package com.farhad.sparkeditableprofile.updateProfile.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer
import com.farhad.sparkeditableprofile.domain.usecase.base.DefaultObserver
import com.farhad.sparkeditableprofile.domain.usecase.getSingleChoiceAnswers.GetSingleChoiceAnswers
import com.farhad.sparkeditableprofile.mapper.SingleChoiceAnswerItemMapper
import com.farhad.sparkeditableprofile.testUtils.FakeSingleChoices
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class UpdateProfileViewModelTest {
    @Mock
    lateinit var getSingleChoiceAnswers: GetSingleChoiceAnswers

    @Mock
    lateinit var singleChoiceAnswerItemMapper: SingleChoiceAnswerItemMapper

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
        val updateProfileViewModel = UpdateProfileViewModel(singleChoiceAnswerItemMapper, getSingleChoiceAnswers)

        assertEquals(singleChoiceItems, updateProfileViewModel.questionSingleChoices.value)
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}