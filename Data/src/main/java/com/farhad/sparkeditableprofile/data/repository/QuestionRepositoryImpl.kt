package com.farhad.sparkeditableprofile.data.repository

import com.farhad.sparkeditableprofile.data.mapper.LocationEntityMapper
import com.farhad.sparkeditableprofile.data.remote.Remote
import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.domain.repository.QuestionRepository
import io.reactivex.Observable

class QuestionRepositoryImpl(private val remote: Remote, private val locationEntityMapper: LocationEntityMapper) :
    QuestionRepository {
    override fun getLocations(): Observable<List<Location>> {
        return remote.getLocations().map { locationEntityMapper.mapToDomain(it) }
    }
}