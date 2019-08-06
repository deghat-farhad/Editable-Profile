package com.farhad.sparkeditableprofile.domain.usecase.uploadProfilePicture


import com.farhad.sparkeditableprofile.domain.model.Profile
import java.io.File

data class UploadProfilePictureParams(
    val picture: File,
    val profile: Profile
)
