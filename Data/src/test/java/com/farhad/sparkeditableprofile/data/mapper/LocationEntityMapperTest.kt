package com.farhad.sparkeditableprofile.data.mapper


import com.farhad.sparkeditableprofile.data.entity.LocationEntity
import com.farhad.sparkeditableprofile.domain.model.Location
import net.bytebuddy.utility.RandomString
import org.junit.Assert.assertEquals
import org.junit.Test

class LocationEntityMapperTest {

    @Test
    fun mapToData() {
        val location = Location(
            RandomString.make(),
            RandomString.make(),
            RandomString.make()
        )

        val locationEntityMapper = LocationEntityMapper()

        val locationEntity = locationEntityMapper.mapToData(location)

        assertEquals(location.city, locationEntity.city)
        assertEquals(location.lat, locationEntity.lat)
        assertEquals(location.lon, locationEntity.lon)
    }

    @Test
    fun mapToDomain() {
        val locationEntity = LocationEntity(
            RandomString.make(),
            RandomString.make(),
            RandomString.make()
        )

        val locationEntityMapper = LocationEntityMapper()

        val location = locationEntityMapper.mapToDomain(locationEntity)

        assertEquals(locationEntity.city, location.city)
        assertEquals(locationEntity.lat, location.lat)
        assertEquals(locationEntity.lon, location.lon)
    }

    @Test
    fun mapLocationEntityListToDomain(){
        val locationEntityList = generateFakeLocationEntityList(100)

        val locationEntityMapper = LocationEntityMapper()

        val locationList = locationEntityMapper.mapToDomain(locationEntityList)

        for (cnt in (0 until locationEntityList.size)){
            assertEquals(locationEntityList[cnt].city, locationList[cnt].city)
            assertEquals(locationEntityList[cnt].lon, locationList[cnt].lon)
            assertEquals(locationEntityList[cnt].lat, locationList[cnt].lat)
        }
    }

    private fun generateFakeLocationEntityList(count: Int): List<LocationEntity> {
        val output = mutableListOf<LocationEntity>()
        (1..count).map { output.add(generateFakeLocationEntity()) }
        return output
    }

    private fun generateFakeLocationEntity()
            = LocationEntity(RandomString.make(), RandomString.make(), RandomString.make())


}