package com.farhad.sparkeditableprofile.mapper

import com.farhad.sparkeditableprofile.domain.model.ProfilePicture
import com.farhad.sparkeditableprofile.model.ProfilePictureItem
import javax.inject.Inject

class ProfilePictureItemMapper @Inject constructor() {
    fun mapToPresentation(profilePicture: ProfilePicture) = ProfilePictureItem(profilePicture.url)
    fun mapToDomain(profilePictureItem: ProfilePictureItem) = ProfilePicture(profilePictureItem.url)
}
