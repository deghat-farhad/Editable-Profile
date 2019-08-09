package com.farhad.sparkeditableprofile.testUtils

import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.model.LocationItem

class FakeLocations {
    fun generateLocationItemList(size: Int): List<LocationItem> {
        return (1..size).map { LocationItem(RandomString().get(), RandomString().get(), RandomString().get()) }
    }

    fun generateLocationList(size: Int): List<Location> {
        return (1..size).map { Location(RandomString().get(), RandomString().get(), RandomString().get()) }
    }
}