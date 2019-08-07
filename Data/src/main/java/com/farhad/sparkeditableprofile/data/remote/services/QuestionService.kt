package com.farhad.sparkeditableprofile.data.remote.services

import com.farhad.sparkeditableprofile.data.entity.LocationEntity
import com.farhad.sparkeditableprofile.data.entity.SingleChoiceAnswerEntity
import io.reactivex.Observable
import retrofit2.http.GET

interface QuestionService {
    @GET("locations.php")
    fun getLocations(): Observable<List<LocationEntity>>

    @GET("single_choice_attributes.php")
    fun getSingleChoiceAnswers(): Observable<Map<String, List<SingleChoiceAnswerEntity>>>

}
