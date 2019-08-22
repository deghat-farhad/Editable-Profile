package com.farhad.sparkeditableprofile.updateProfile.viewModel.validator

import com.farhad.sparkeditableprofile.model.LocationItem
import javax.inject.Inject
import javax.inject.Named

class LocationValidator @Inject constructor(@Named("notAValidCity") val emptyMessage: String) {

    @Throws(ValidationException::class)
    fun validate(locationItem: LocationItem?) {
        if (locationItem == null)
            throw ValidationException(emptyMessage)
    }

}