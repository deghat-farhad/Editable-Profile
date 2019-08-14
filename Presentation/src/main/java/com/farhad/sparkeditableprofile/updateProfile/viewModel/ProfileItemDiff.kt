package com.farhad.sparkeditableprofile.updateProfile.viewModel

import com.farhad.sparkeditableprofile.model.LocationItem
import com.farhad.sparkeditableprofile.model.ProfileItem
import com.farhad.sparkeditableprofile.model.ProfilePictureItem
import com.farhad.sparkeditableprofile.model.SingleChoiceAnswerItem
import java.util.*
import javax.inject.Inject

open class ProfileItemDiff @Inject constructor(){
    fun getDiff(oldProfileItem: ProfileItem, newProfileItem: ProfileItem): ProfileItem {
        return ProfileItem(
            oldProfileItem.id,
            getStringDiff(oldProfileItem.displayName, newProfileItem.displayName),
            getStringDiff(oldProfileItem.realName, newProfileItem.realName),
            getProfilePictureDiff(oldProfileItem.profilePicture, newProfileItem.profilePicture),
            newProfileItem.birthday,
            null,
            getStringDiff(oldProfileItem.occupation, newProfileItem.occupation),
            getStringDiff(oldProfileItem.aboutMe, newProfileItem.aboutMe),
            getLocationDiff(oldProfileItem.location, newProfileItem.location),
            getHashMapSingleChoiceDiff(oldProfileItem.answers, newProfileItem.answers)
        )
    }

    private fun getProfilePictureDiff(
        oldProfilePicture: ProfilePictureItem?,
        newProfilePicture: ProfilePictureItem?
    ): ProfilePictureItem? {
        return when {
            oldProfilePicture == newProfilePicture -> null
            newProfilePicture == null -> ProfilePictureItem("")
            else -> newProfilePicture
        }
    }

    private fun getStringDiff(oldString: String?, newString: String?): String? {

        return when {
            oldString == newString -> null
            newString == null -> ""
            else -> newString
        }
    }

    private fun getLocationDiff(oldLocationItem: LocationItem?, newLocationItem: LocationItem?): LocationItem? {
        return when {
            oldLocationItem == newLocationItem -> null
            newLocationItem == null -> LocationItem("", "", "")
            else -> newLocationItem
        }
    }

    private fun getHashMapSingleChoiceDiff(
        oldSingleChoiceAnswers: HashMap<String, SingleChoiceAnswerItem>,
        newSingleChoiceAnswers: HashMap<String, SingleChoiceAnswerItem>
    ): HashMap<String, SingleChoiceAnswerItem> {
        val output = HashMap<String, SingleChoiceAnswerItem>()
        for (singleChoiceAnswerKey in newSingleChoiceAnswers.keys) {
            if(oldSingleChoiceAnswers[singleChoiceAnswerKey] != newSingleChoiceAnswers[singleChoiceAnswerKey]){
                newSingleChoiceAnswers[singleChoiceAnswerKey]?.let{
                    output[singleChoiceAnswerKey] = it
                }
            }
        }
        for (singleChoiceAnswerKey in oldSingleChoiceAnswers.keys){
            if(!newSingleChoiceAnswers.keys.contains(singleChoiceAnswerKey)){
                output[singleChoiceAnswerKey] = SingleChoiceAnswerItem("", "")
            }
        }
        return output
    }
}