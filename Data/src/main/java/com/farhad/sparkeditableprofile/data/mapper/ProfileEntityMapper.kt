package com.farhad.sparkeditableprofile.data.mapper

import com.farhad.sparkeditableprofile.data.entity.LocationEntity
import com.farhad.sparkeditableprofile.data.entity.ProfileEntity
import com.farhad.sparkeditableprofile.data.entity.ProfilePictureEntity
import com.farhad.sparkeditableprofile.domain.model.Profile

class ProfileEntityMapper(
    private val locationEntityMapper: LocationEntityMapper,
    private val singleChoiceAnswerEntityMapper: SingleChoiceAnswerEntityMapper,
    private val profilePictureEntityMapper: ProfilePictureEntityMapper
){
    fun mapToData(profile: Profile): ProfileEntity {
        var locationEntity: LocationEntity? = null
        profile.location?.let { locationEntity = locationEntityMapper.mapToData(it) }

        var profilePictureEntity: ProfilePictureEntity? = null
        profile.profilePicture?.let { profilePictureEntity = profilePictureEntityMapper.mapToData(it) }

        return ProfileEntity(
            profile.id,
            profile.displayName,
            profile.realName,
            profilePictureEntity,
            profile.birthday,
            profile.height,
            profile.occupation,
            profile.aboutMe,
            locationEntity,
            singleChoiceAnswerEntityMapper.mapToData(profile.answers)
        )
    }
}