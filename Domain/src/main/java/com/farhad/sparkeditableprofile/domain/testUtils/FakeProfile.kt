package com.farhad.sparkeditableprofile.domain.testUtils

import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.domain.model.ProfilePicture
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt

class FakeProfile {
    val id = RandomString().get(64)
    val displayName = RandomString().get()
    val realName = RandomString().get()
    val occupation = RandomString().get()
    val aboutMe = RandomString().get()
    val selectedLocation = Location(RandomString().get(), RandomString().get(), RandomString().get())
    val height = Random.nextInt(100..180)
    val selectedAnswers = FakeSingleChoice().generateFakeSingleChoiceAnswerMap(10)
    val profilePicture = ProfilePicture("url/of/profile/pic")
    val birthDay = Date(0)

    fun getProfile(): com.farhad.sparkeditableprofile.domain.model.Profile {
        return com.farhad.sparkeditableprofile.domain.model.Profile(
            id, displayName, realName, profilePicture, birthDay,
            height, occupation, aboutMe, selectedLocation, selectedAnswers
        )
    }
}