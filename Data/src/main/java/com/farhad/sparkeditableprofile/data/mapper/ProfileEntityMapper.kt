package com.farhad.sparkeditableprofile.data.mapper

import com.farhad.sparkeditableprofile.data.entity.LocationEntity
import com.farhad.sparkeditableprofile.data.entity.ProfileEntity
import com.farhad.sparkeditableprofile.data.entity.ProfilePictureEntity
import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.domain.model.Profile
import com.farhad.sparkeditableprofile.domain.model.ProfilePicture

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

    fun mapToDomain(profileEntity: ProfileEntity): Profile {
        var location: Location? = null
        profileEntity.location?.let { location = locationEntityMapper.mapToDomain(it) }

        var profilePicture: ProfilePicture? = null
        profileEntity.Profile_Picture?.let { profilePicture = profilePictureEntityMapper.mapToDomain(it) }

        return Profile(
            profileEntity.id,
            profileEntity.Display_Name,
            profileEntity.Real_Name,
            profilePicture,
            profileEntity.Birthday,
            profileEntity.Height,
            profileEntity.Occupation,
            profileEntity.About_Me,
            location,
            singleChoiceAnswerEntityMapper.mapToDomain(profileEntity.answers)
        )
    }
}