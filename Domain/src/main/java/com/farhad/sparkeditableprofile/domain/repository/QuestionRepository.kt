package com.farhad.sparkeditableprofile.domain.repository

import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer
import io.reactivex.Observable

interface QuestionRepository {
    fun getLocations(): Observable<List<Location>>
    fun getSingleChoiceAnswers(): Observable<HashMap<String, List<SingleChoiceAnswer>>>
}
