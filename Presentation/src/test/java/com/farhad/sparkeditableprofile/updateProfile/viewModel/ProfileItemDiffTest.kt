package com.farhad.sparkeditableprofile.updateProfile.viewModel

import com.farhad.sparkeditableprofile.model.ProfileItem
import com.farhad.sparkeditableprofile.model.SingleChoiceAnswerItem
import com.farhad.sparkeditableprofile.testUtils.FakeLocations
import com.farhad.sparkeditableprofile.testUtils.FakeProfile
import com.farhad.sparkeditableprofile.testUtils.FakeSingleChoices
import com.farhad.sparkeditableprofile.testUtils.RandomString
import org.junit.Test

import org.junit.Assert.*

class ProfileItemDiffTest {

    @Test
    fun getDiff() {
        val locationItems = FakeLocations().generateLocationItemList(100).toMutableList()
        val singleChoiceItems = FakeSingleChoices().generateFakeSingleChoiceAnswerItemListMap(10, 8)
        val oldProfileItem = FakeProfile(locationItems, singleChoiceItems).getProfileItem()
        val newProfielItem = FakeProfile(locationItems, singleChoiceItems).getProfileItem()
        val nullProfile = ProfileItem(RandomString().get())

        checkDiff(oldProfileItem, newProfielItem)
        checkDiff(oldProfileItem, oldProfileItem)
        checkDiff(oldProfileItem, nullProfile)

    }

    private fun checkDiff(oldProfileItem: ProfileItem, newProfileItem: ProfileItem) {
        val diffProfile = ProfileItemDiff().getDiff(oldProfileItem, newProfileItem)

        assertEquals(newProfileItem.birthday, diffProfile.birthday)


        when {
            newProfileItem.displayName == null -> assertEquals("", diffProfile.displayName)
            newProfileItem.displayName != oldProfileItem.displayName -> assertEquals(
                newProfileItem.displayName,
                diffProfile.displayName
            )
            else -> assertNull(diffProfile.displayName)
        }

        when {
            newProfileItem.realName == null -> assertEquals("", diffProfile.realName)
            newProfileItem.realName != oldProfileItem.realName -> assertEquals(
                newProfileItem.realName,
                diffProfile.realName
            )
            else -> assertNull(diffProfile.realName)
        }

        when {
            newProfileItem.occupation == null -> assertEquals("", diffProfile.occupation)
            newProfileItem.occupation != oldProfileItem.occupation -> assertEquals(
                newProfileItem.occupation,
                diffProfile.occupation
            )
            else -> assertNull(diffProfile.occupation)
        }

        when {
            newProfileItem.aboutMe == null -> assertEquals("", diffProfile.aboutMe)
            newProfileItem.aboutMe != oldProfileItem.aboutMe -> assertEquals(
                newProfileItem.aboutMe,
                diffProfile.aboutMe
            )
            else -> assertNull(diffProfile.aboutMe)
        }

        when {
            newProfileItem.location == null -> {
                assertEquals("", diffProfile.location?.lat)
                assertEquals("", diffProfile.location?.lon)
                assertEquals("", diffProfile.location?.city)
            }
            newProfileItem.location != oldProfileItem.location -> {
                assertEquals(newProfileItem.location?.lat, diffProfile.location?.lat)
                assertEquals(newProfileItem.location?.lon, diffProfile.location?.lon)
                assertEquals(newProfileItem.location?.city, diffProfile.location?.city)
            }
            else -> assertNull(diffProfile.location)
        }

        for (question in newProfileItem.answers.keys) {
            if (oldProfileItem.answers[question] != newProfileItem.answers[question]) {
                assertEquals(newProfileItem.answers[question], diffProfile.answers[question])
            }
        }
        for (question in oldProfileItem.answers.keys) {
            if (!oldProfileItem.answers.keys.contains(question)) {
                assertEquals("", diffProfile.answers[question]?.name)
                assertEquals("", diffProfile.answers[question]?.id)
            }
        }
    }
}
