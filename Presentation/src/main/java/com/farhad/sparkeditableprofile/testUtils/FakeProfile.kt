package com.farhad.sparkeditableprofile.testUtils

import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.domain.model.Profile
import com.farhad.sparkeditableprofile.domain.model.ProfilePicture
import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer
import com.farhad.sparkeditableprofile.model.LocationItem
import com.farhad.sparkeditableprofile.model.ProfileItem
import com.farhad.sparkeditableprofile.model.ProfilePictureItem
import com.farhad.sparkeditableprofile.model.SingleChoiceAnswerItem
import java.util.*
import kotlin.collections.HashMap
import kotlin.random.Random
import kotlin.random.nextInt

class FakeProfile(
    val locationItems: List<LocationItem>,
    singleChoicesItems: HashMap<String, List<SingleChoiceAnswerItem>>
) {

    val id = RandomString().get(64)
    val displayName = RandomString().get()
    val realName = RandomString().get()
    val occupation = RandomString().get()
    val aboutMe = RandomString().get()
    val selectedLocationItem = locationItems[Random.nextInt(0 until locationItems.size)]
    val selectedLocation = Location(selectedLocationItem.lat, selectedLocationItem.lon, selectedLocationItem.city)
    val height = Random.nextInt(100..180)
    val selectedAnswers = HashMap<String, SingleChoiceAnswer>()
    val selectedAnswerItems = HashMap<String, SingleChoiceAnswerItem>()
    val profilePicture = ProfilePicture(null)
    val profilePictureItem = ProfilePictureItem(null)
    val birthDay = Date(0)

    init {

        for (question in singleChoicesItems.keys) {
            singleChoicesItems[question]?.let {
                val answerIndex = Random.nextInt(0 until it.size)
                selectedAnswers[question] = SingleChoiceAnswer(it[answerIndex].id, it[answerIndex].name)
                selectedAnswerItems[question] = it[answerIndex]
            }
        }
    }

    fun getProfile(): Profile {
        return Profile(
            id, displayName, realName, profilePicture, birthDay,
            height, occupation, aboutMe, selectedLocation, selectedAnswers
        )
    }

    fun getProfileItem(): ProfileItem {
        return ProfileItem(
            id, displayName, realName, profilePictureItem, birthDay,
            height, occupation, aboutMe, selectedLocationItem, selectedAnswerItems
        )
    }
}