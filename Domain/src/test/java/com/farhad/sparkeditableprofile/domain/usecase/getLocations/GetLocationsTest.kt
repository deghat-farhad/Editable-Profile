package com.farhad.sparkeditableprofile.domain.usecase.getLocations

import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.domain.repository.QuestionRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import net.bytebuddy.utility.RandomString
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GetLocationsTest {
    @Mock lateinit var scheduler: Scheduler
    @Mock lateinit var questionRepository: QuestionRepository

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun buildUseCaseObservable() {
        val locationList = generateFakeLocationList(100)

        Mockito.`when`(questionRepository.getLocations())
            .thenReturn(Observable.just(locationList))


        val getLocations = GetLocations(scheduler, scheduler, questionRepository)

        val observableLocationList = getLocations.buildUseCaseObservable(Unit)

        observableLocationList.test().assertValue {
            it == locationList
        }

        Mockito.verify(questionRepository).getLocations()
    }

    private fun generateFakeLocationList(count: Int): List<Location> {
        val output = mutableListOf<Location>()
        (1..count).map { output.add(generateFakeLocation()) }
        return output
    }

    private fun generateFakeLocation() = Location(RandomString.make(), RandomString.make(), RandomString.make())
}