package com.farhad.sparkeditableprofile.data.remote.Services

import com.farhad.sparkeditableprofile.data.entity.ProfileEntity
import com.farhad.sparkeditableprofile.data.entity.RequestStatusEntity
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface ProfileService {
    @POST("user.php")
    fun registerProfile(@Body profileEntity: ProfileEntity)
            : Observable<RequestStatusEntity>
}
