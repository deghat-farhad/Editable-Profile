package com.farhad.sparkeditableprofile.data.repository

import com.farhad.sparkeditableprofile.data.entity.LocationEntity
import com.farhad.sparkeditableprofile.data.mapper.LocationEntityMapper
import com.farhad.sparkeditableprofile.data.remote.Remote
import com.farhad.sparkeditableprofile.domain.model.Location
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

        val questionRepositoryImpl = QuestionRepositoryImpl(remote, locationEntityMapper)
        val observableLocationList = questionRepositoryImpl.getLocations()

        observableLocationList.test().assertValue {
            isEqual(locationEntityList, it)
        }.onComplete()
    }

    private fun isEqual(locationEntityList: List<LocationEntity>, locationList: List<Location>): Boolean {
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