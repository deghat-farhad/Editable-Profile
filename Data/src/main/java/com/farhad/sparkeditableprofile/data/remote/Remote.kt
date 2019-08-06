package com.farhad.sparkeditableprofile.data.remote

import com.farhad.sparkeditableprofile.data.entity.ProfileEntity
import com.farhad.sparkeditableprofile.data.entity.ProfilePictureEntity
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class Remote(private val serviceGenerator: ServiceGenerator) {
    fun registerProfile(profileEntity: ProfileEntity) = serviceGenerator.profileService().registerProfile(profileEntity)

    fun uploadProfilePicture(picture: File, profileId: String): Observable<ProfilePictureEntity> {
        val requestBody = RequestBody.create(MediaType.parse("*/*"), picture)
        val fileToUpload = MultipartBody.Part.createFormData(
            "file",
            profileId + "." + picture.extension, requestBody
        )
        return serviceGenerator.profileService().uploadProfilePicture(fileToUpload)
    }
}
