package com.farhad.sparkeditableprofile.data.mapper

import com.farhad.sparkeditableprofile.data.entity.ProfilePictureEntity
import com.farhad.sparkeditableprofile.domain.model.ProfilePicture

class ProfilePictureEntityMapper {
    fun mapToDomain(profilePictureEntity: ProfilePictureEntity) = ProfilePicture(profilePictureEntity.url)
    fun mapToData(profilePicture: ProfilePicture) = ProfilePictureEntity(profilePicture.url)
}
