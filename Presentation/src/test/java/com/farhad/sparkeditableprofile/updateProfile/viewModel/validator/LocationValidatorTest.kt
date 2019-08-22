package com.farhad.sparkeditableprofile.updateProfile.viewModel.validator

import com.farhad.sparkeditableprofile.model.LocationItem
import com.farhad.sparkeditableprofile.testUtils.RandomString
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class LocationValidatorTest {

    @Rule
    @JvmField
    var expectionEx = ExpectedException.none()

    @Test
    fun validateNull() {
        val message = RandomString().get()
        expectionEx.expect(ValidationException::class.java)
        expectionEx.expectMessage(message)

        LocationValidator(message).validate(null)
    }

    @Test
    fun validate() {
        LocationValidator(RandomString().get()).validate(
            LocationItem(
                RandomString().get(),
                RandomString().get(),
                RandomString().get()
            )
        )
    }
}