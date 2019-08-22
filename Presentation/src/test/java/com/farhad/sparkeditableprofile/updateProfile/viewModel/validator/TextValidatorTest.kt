package com.farhad.sparkeditableprofile.updateProfile.viewModel.validator

import com.farhad.sparkeditableprofile.testUtils.RandomString
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException


class TextValidatorTest {

    @Rule
    @JvmField
    var expectionEx = ExpectedException.none()

    @Test
    fun isEmptyTest() {
        val text = ""
        val notEmptyMessage = RandomString().get()
        val tooLongMessage = RandomString().get()
        val invalidChars = RandomString().get()


        expectionEx.expect(ValidationException::class.java)
        expectionEx.expectMessage(notEmptyMessage)

        TextValidator(notEmptyMessage, tooLongMessage, invalidChars).validate(text, 1, false)
    }

    @Test
    fun isLongTest() {
        val text = "qw"
        val maxLength = 1
        val notEmptyMessage = RandomString().get()
        val tooLongMessage = RandomString().get()
        val invalidChars = RandomString().get()

        expectionEx.expect(ValidationException::class.java)
        expectionEx.expectMessage(tooLongMessage)

        TextValidator(notEmptyMessage, tooLongMessage, invalidChars).validate(text, maxLength, false)
    }

    @Test
    fun hasWrongPatternTest() {
        val text = "A"
        val pattern = Regex("a-z")
        val notEmptyMessage = RandomString().get()
        val tooLongMessage = RandomString().get()
        val invalidChars = RandomString().get()

        expectionEx.expect(ValidationException::class.java)
        expectionEx.expectMessage(invalidChars)

        TextValidator(notEmptyMessage, tooLongMessage, invalidChars).validate(text, 1, false, pattern)
    }

    @Test
    fun isValidateTest() {
        val text = "asdfasdfasdf"
        val pattern = Regex("[a-z]*")
        val notEmptyMessage = RandomString().get()
        val tooLongMessage = RandomString().get()
        val invalidChars = RandomString().get()

        TextValidator(notEmptyMessage, tooLongMessage, invalidChars).validate(text, 100, false, pattern)
    }
}