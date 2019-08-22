package com.farhad.sparkeditableprofile.updateProfile.viewModel.validator

import javax.inject.Inject
import javax.inject.Named

class TextValidator @Inject constructor(
    @Named("notEmptyMessage") val notEmptyMessage: String,
    @Named("tooLongMessage") val tooLongMessage: String,
    @Named("charactersNotAllowedMessage") val charactersNotAllowedMessage: String
    ) {
    @Throws(ValidationException::class)
    fun validate(text: String, maxLength: Int, emptyAllowed: Boolean, patern: Regex = Regex(".*")) {
        if (!emptyAllowed && text.isEmpty())
            throw ValidationException(notEmptyMessage)
        if (text.length > maxLength)
            throw ValidationException(tooLongMessage)
        if (!patern.matches(text))
            throw ValidationException(charactersNotAllowedMessage)
    }
}