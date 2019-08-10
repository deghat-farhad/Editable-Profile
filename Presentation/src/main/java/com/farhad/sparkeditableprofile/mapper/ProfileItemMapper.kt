package com.farhad.sparkeditableprofile.mapper

import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.domain.model.Profile
import com.farhad.sparkeditableprofile.domain.model.ProfilePicture
import com.farhad.sparkeditableprofile.model.LocationItem
import com.farhad.sparkeditableprofile.model.ProfileItem
import com.farhad.sparkeditableprofile.model.ProfilePictureItem
import javax.inject.Inject

class ProfileItemMapper @Inject constructor(
    private val locationItemMapper: LocationItemMapper,
    private val singleChoiceAnswerItemMapper: SingleChoiceAnswerItemMapper,
    private val profilePictureItemMapper: ProfilePictureItemMapper
) {
    fun mapToPresentation(profile: Profile): ProfileItem {

        var locationItem: LocationItem? = null
        profile.location?.let {
            locationItem = locationItemMapper.mapToPresentation(it)
        }

        var profileProfileItem: ProfilePictureItem? = null
        profile.profilePicture?.let {
            profileProfileItem = profilePictureItemMapper.mapToPresentation(it)
        }

        return ProfileItem(
            profile.id,
            profile.displayName,
            profile.realName,
            profileProfileItem,
            profile.birthday,
            profile.height,
            profile.occupation,
            profile.aboutMe,
            locationItem,
            singleChoiceAnswerItemMapper.mapToPresentation(profile.answers)
        )
    }

    fun mapToDomain(profileItem: ProfileItem): Profile {
        var location: Location? = null
        profileItem.location?.let { location = locationItemMapper.mapToDomain(it) }

        var profileProfile: ProfilePicture? = null
        profileItem.profilePicture?.let {
            profileProfile = profilePictureItemMapper.mapToDomain(it)
        }

        return Profile(
            profileItem.id,
            profileItem.displayName,
            profileItem.realName,
            profileProfile,
            profileItem.birthday,
            profileItem.height,
            profileItem.occupation,
            profileItem.aboutMe,
            location,
            singleChoiceAnswerItemMapper.mapToDomain(profileItem.answers)
        )
    }
}