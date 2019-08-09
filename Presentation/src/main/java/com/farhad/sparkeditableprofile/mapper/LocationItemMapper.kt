package com.farhad.sparkeditableprofile.mapper

import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.model.LocationItem
import javax.inject.Inject

class LocationItemMapper @Inject constructor() {
    fun mapToPresentation(location: Location)
            = LocationItem(location.lat, location.lon, location.city)

    fun mapToPresentation(locations: List<Location>) = locations.map{mapToPresentation(it)}

}
