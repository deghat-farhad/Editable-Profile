package com.farhad.sparkeditableprofile.data.remote.services

import com.farhad.sparkeditableprofile.data.entity.LocationEntity
import io.reactivex.Observable
import retrofit2.http.GET

interface QuestionService {
    @GET("locations.php")
    fun getLocations(): Observable<List<LocationEntity>>
}
