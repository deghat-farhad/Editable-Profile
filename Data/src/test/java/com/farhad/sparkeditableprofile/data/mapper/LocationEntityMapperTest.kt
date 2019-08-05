package com.farhad.sparkeditableprofile.data.mapper


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

}