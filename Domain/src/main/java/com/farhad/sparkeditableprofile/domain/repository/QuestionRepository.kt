package com.farhad.sparkeditableprofile.domain.repository

import com.farhad.sparkeditableprofile.domain.model.Location
import io.reactivex.Observable

interface QuestionRepository {
    fun getLocations(): Observable<List<Location>>
}
