package com.farhad.sparkeditableprofile.data.remote.services

import com.farhad.sparkeditableprofile.data.entity.ProfileEntity
import com.farhad.sparkeditableprofile.data.entity.ProfilePictureEntity
import com.farhad.sparkeditableprofile.data.entity.RequestStatusEntity
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ProfileService {
    @POST("user.php")
    fun registerProfile(@Body profileEntity: ProfileEntity)
            : Observable<RequestStatusEntity>

    @Multipart
    @POST("upload_profile_picture.php")
    fun uploadProfilePicture(@Part picture: MultipartBody.Part)
            : Observable<ProfilePictureEntity>
}
