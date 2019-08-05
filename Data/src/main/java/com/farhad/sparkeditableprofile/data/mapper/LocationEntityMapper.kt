package com.farhad.sparkeditableprofile.data.mapper

import com.farhad.sparkeditableprofile.data.entity.LocationEntity
import com.farhad.sparkeditableprofile.domain.model.Location

class LocationEntityMapper {
    fun mapToData(location: Location) = LocationEntity(location.lat, location.lon, location.city)
}
