package com.farhad.sparkeditableprofile.data.repository

import com.farhad.sparkeditableprofile.data.entity.LocationEntity
import com.farhad.sparkeditableprofile.data.entity.SingleChoiceAnswerEntity
import com.farhad.sparkeditableprofile.data.mapper.LocationEntityMapper
import com.farhad.sparkeditableprofile.data.mapper.SingleChoiceAnswerEntityMapper
import com.farhad.sparkeditableprofile.data.remote.Remote
import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer
import io.reactivex.Observable
import net.bytebuddy.utility.RandomString
import org.junit.Before
import org.junit.Test

import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class QuestionRepositoryImplTest {
    @Mock lateinit var remote: Remote
    @Mock lateinit var locationEntityMapper: LocationEntityMapper
    @Mock lateinit var singleChoiceAnswerEntityMapper: SingleChoiceAnswerEntityMapper

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getLocations() {
        val locationEntityList = generateFakeLocationList(100)
        Mockito.`when`(locationEntityMapper.mapToDomain(any<List<LocationEntity>>()))
            .thenReturn(mapLocationEntityListToLocationList(locationEntityList))
        Mockito.`when`(remote.getLocations()).thenReturn(Observable.just(listOf()))

        val questionRepositoryImpl
                = QuestionRepositoryImpl(remote, locationEntityMapper, singleChoiceAnswerEntityMapper)
        val observableLocationList = questionRepositoryImpl.getLocations()

        observableLocationList.test().assertValue {
            isEqualLocationLists(locationEntityList, it)
        }.onComplete()
    }

    @Test
    fun getSingleChoiceAnswers(){
         val singleChoiceAnswerEntityMap
                 = generateFakesingleChoiceAnswerEntityMap(10 ,10)
        Mockito.`when`(singleChoiceAnswerEntityMapper
            .mapListToDomain(any<HashMap<String, List<SingleChoiceAnswerEntity>>>()))
            .thenReturn(singleChoiceAnswerEntityMap)
        Mockito.`when`(remote.getSingleChoiceAnswers()).thenReturn(Observable.just(hashMapOf()))

        val questionRepositoryImpl
                = QuestionRepositoryImpl(remote, locationEntityMapper, singleChoiceAnswerEntityMapper)

        val observableSingleChoiceAnswerMap
                = questionRepositoryImpl.getSingleChoiceAnswers()

        observableSingleChoiceAnswerMap.test().assertValue {
            singleChoiceAnswerEntityMap == it
        }.onComplete()
    }

    private fun generateFakesingleChoiceAnswerEntityMap(questionCnt: Int, answerCnt: Int): HashMap<String, List<SingleChoiceAnswer>>{
            val output = HashMap<String, List<SingleChoiceAnswer>>()

            for (questionCntr in (1 .. questionCnt)){
                val question = RandomString.make()
                val answerList = mutableListOf<SingleChoiceAnswer>()
                for (answerCntr in (1 .. answerCnt)){
                    answerList.add(
                        SingleChoiceAnswer(RandomString.make(), RandomString.make())
                    )
                }
                output[question] = answerList
            }

            return output
    }

    private fun isEqualLocationLists(locationEntityList: List<LocationEntity>, locationList: List<Location>): Boolean {
        for(cnt in (0 until locationList.size)){
            if(
                locationList[cnt].city != locationEntityList[cnt].city ||
                locationList[cnt].city != locationEntityList[cnt].city ||
                locationList[cnt].city != locationEntityList[cnt].city
            ) return false
        }
        return true
    }

    private fun generateFakeLocationList(count: Int): List<LocationEntity> {
        val output = mutableListOf<LocationEntity>()
        (1..count).map { output.add(generateFakeLocationEntity()) }
        return output
    }

    private fun generateFakeLocationEntity()
            = LocationEntity(RandomString.make(), RandomString.make(), RandomString.make())

    private fun mapLocationEntityListToLocationList(locationEntityList: List<LocationEntity>): List<Location>{
        return locationEntityList.map { Location(it.lat, it.lon, it.city) }
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }
    private fun <T> uninitialized(): T = null as T
}