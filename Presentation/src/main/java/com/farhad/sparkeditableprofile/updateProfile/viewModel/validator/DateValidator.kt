package com.farhad.sparkeditableprofile.updateProfile.viewModel.validator

import java.util.*
import javax.inject.Inject
import javax.inject.Named

class DateValidator @Inject constructor(
    @Named("notEmptyMessage") val notEmptyMessage: String,
    @Named("youngerMessage") val youngerMessage: String
) {

    @Throws(ValidationException::class)
    fun confirmIsOlderThan(date: Date?, minAge: Int) {
        if (date == null)
            throw ValidationException(notEmptyMessage)
        val calendar = Calendar.getInstance()
        val now = Date()
        val diffInMillies = Math.abs(now.time - date.time)
        calendar.timeInMillis = diffInMillies
        val diff = calendar.get(Calendar.YEAR) - 1970

        if (diff < minAge && now.after(date)) {
            throw (ValidationException(String.format(youngerMessage, minAge)))
        }
    }
}