package com.farhad.sparkeditableprofile.updateProfile.viewModel.validator

import com.farhad.sparkeditableprofile.testUtils.RandomString
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.util.*

class DateValidatorTest {

    @Rule
    @JvmField
    var expectionEx = ExpectedException.none()

    @Test
    fun confirmIsOlderThanYounger() {
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -1)
        val minAge = 20
        val youngerMessage = RandomString().get() + "%d"
        val emptyMessage = RandomString().get()

        expectionEx.expect(ValidationException::class.java)
        expectionEx.expectMessage(String.format(youngerMessage, minAge))
        DateValidator(emptyMessage, youngerMessage).confirmIsOlderThan(cal.time, minAge)
    }

    @Test
    fun confirmIsOlderThanNull() {
        val minAge = 20
        val youngerMessage = RandomString().get() + "%d"
        val emptyMessage = RandomString().get()

        expectionEx.expect(ValidationException::class.java)
        expectionEx.expectMessage(emptyMessage)
        DateValidator(emptyMessage, youngerMessage).confirmIsOlderThan(null, minAge)
    }

    @Test
    fun confirmIsOlderThan() {
        val now = Date(0)
        val minAge = 20
        val youngerMessage = RandomString().get() + "%d"
        val emptyMessage = RandomString().get()

        DateValidator(emptyMessage, youngerMessage).confirmIsOlderThan(now, minAge)
    }
}