package com.farhad.sparkeditableprofile.data.remote.services

import com.farhad.sparkeditableprofile.data.entity.ProfileEntity
import com.farhad.sparkeditableprofile.data.entity.ProfilePictureEntity
import com.farhad.sparkeditableprofile.data.entity.RequestStatusEntity
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface ProfileService {
    @POST("user.php")
    fun registerProfile(@Body profileEntity: ProfileEntity)
            : Observable<RequestStatusEntity>

    @Multipart
    @POST("upload_profile_picture.php")
    fun uploadProfilePicture(@Part picture: MultipartBody.Part)
            : Observable<ProfilePictureEntity>

    @GET("user.php/{userId}")
    fun getProfile(@Path("userId") userId: String): Observable<ProfileEntity>

    @PUT("user.php")
    fun updateProfile(@Body profileEntity: ProfileEntity)
            : Observable<RequestStatusEntity>
}
