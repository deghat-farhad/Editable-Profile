package com.farhad.sparkeditableprofile.data.repository

import com.farhad.sparkeditableprofile.data.mapper.LocationEntityMapper
import com.farhad.sparkeditableprofile.data.mapper.SingleChoiceAnswerEntityMapper
import com.farhad.sparkeditableprofile.data.remote.Remote
import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer
import com.farhad.sparkeditableprofile.domain.repository.QuestionRepository
import io.reactivex.Observable

class QuestionRepositoryImpl(
    private val remote: Remote,
    private val locationEntityMapper: LocationEntityMapper,
    private val singleChoiceAnswerEntityMapper: SingleChoiceAnswerEntityMapper) :
    QuestionRepository {
    override fun getLocations(): Observable<List<Location>> {
        return remote.getLocations().map { locationEntityMapper.mapToDomain(it) }
    }

    override fun getSingleChoiceAnswers(): Observable<HashMap<String, List<SingleChoiceAnswer>>> {
        return remote.getSingleChoiceAnswers().map { singleChoiceAnswerEntityMapper.mapListToDomain(it)}
    }
}