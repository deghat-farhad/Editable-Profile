package com.farhad.sparkeditableprofile.data.testUtils

import com.farhad.sparkeditableprofile.data.entity.LocationEntity

class FakeLocation {
    private fun generateFakeLocationEntity() =
        LocationEntity(RandomString().get(), RandomString().get(), RandomString().get())

    fun generateFakeLocationList(count: Int): List<LocationEntity> {
        val output = mutableListOf<LocationEntity>()
        (1..count).map { output.add(generateFakeLocationEntity()) }
        return output
    }
}