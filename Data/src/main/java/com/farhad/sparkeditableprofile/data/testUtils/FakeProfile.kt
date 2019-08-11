package com.farhad.sparkeditableprofile.data.testUtils

import com.farhad.sparkeditableprofile.data.entity.LocationEntity
import com.farhad.sparkeditableprofile.data.entity.ProfileEntity
import com.farhad.sparkeditableprofile.data.entity.ProfilePictureEntity
import com.farhad.sparkeditableprofile.data.entity.SingleChoiceAnswerEntity
import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.domain.model.Profile
import com.farhad.sparkeditableprofile.domain.model.ProfilePicture
import java.util.*
import kotlin.collections.HashMap
import kotlin.random.Random
import kotlin.random.nextInt

class FakeProfile() {

    val id = RandomString().get(64)
    val displayName = RandomString().get()
    val realName = RandomString().get()
    val occupation = RandomString().get()
    val aboutMe = RandomString().get()
    val selectedLocationEntity = LocationEntity(RandomString().get(), RandomString().get(), RandomString().get())
    val selectedLocation = Location(selectedLocationEntity.lat, selectedLocationEntity.lon, selectedLocationEntity.city)
    val height = Random.nextInt(100..180)
    val selectedAnswers = FakeSingleChoices().generateFakeSingleChoiceAnswerMap(10)
    val selectedAnswerEntities = HashMap<String, SingleChoiceAnswerEntity>()
    val profilePicture = ProfilePicture("url/of/profile/pic")
    val profilePictureEntity = ProfilePictureEntity("url/of/profile/pic")
    val birthDay = Date(0)

    init {
        for (question in selectedAnswers.keys) {
            selectedAnswerEntities[question]?.let {
                SingleChoiceAnswerEntity(it.id, it.name)
            }
        }
    }

    fun getProfile(): Profile {
        return Profile(
            id, displayName, realName, profilePicture, birthDay,
            height, occupation, aboutMe, selectedLocation, selectedAnswers
        )
    }

    fun getProfileEntity(): ProfileEntity {
        return ProfileEntity(
            id, displayName, realName, profilePictureEntity, birthDay,
            height, occupation, aboutMe, selectedLocationEntity, selectedAnswerEntities
        )
    }
}