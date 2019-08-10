package com.farhad.sparkeditableprofile.mapper

import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.model.LocationItem
import com.farhad.sparkeditableprofile.testUtils.FakeLocations
import com.farhad.sparkeditableprofile.testUtils.RandomString
import org.junit.Test

import org.junit.Assert.*

class LocationItemMapperTest {
    private val location = Location(RandomString().get(), RandomString().get(), RandomString().get())
    private val locationList = FakeLocations().generateLocationList(100)

    @Test
    fun mapToPresentation() {
        val locationItem = LocationItemMapper().mapToPresentation(location)

        assertEquals(location.city, locationItem.city)
        assertEquals(location.lat, locationItem.lat)
        assertEquals(location.lon, locationItem.lon)
    }

    @Test
    fun mapListToPresentation() {
        val locationItemList = LocationItemMapper().mapToPresentation(locationList)

        for(cnt in (0 until locationList.size)){
            assertEquals(locationList[cnt].city, locationItemList[cnt].city)
            assertEquals(locationList[cnt].lat, locationItemList[cnt].lat)
            assertEquals(locationList[cnt].lon, locationItemList[cnt].lon)
        }
    }

    @Test
    fun mapToDomain(){
        val locationItem = LocationItem(
            RandomString().get(),
            RandomString().get(),
            RandomString().get()
        )

        val location = LocationItemMapper().mapToDomain(locationItem)

        assertEquals(locationItem.city, location.city)
        assertEquals(locationItem.lat, location.lat)
        assertEquals(locationItem.lon, location.lon)
    }
}