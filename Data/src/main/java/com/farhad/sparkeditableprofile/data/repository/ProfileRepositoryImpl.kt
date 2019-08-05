package com.farhad.sparkeditableprofile.data.repository

import com.farhad.sparkeditableprofile.data.mapper.ProfileEntityMapper
import com.farhad.sparkeditableprofile.data.mapper.RequestStatusEntityMapper
import com.farhad.sparkeditableprofile.data.remote.Remote
import com.farhad.sparkeditableprofile.domain.model.Profile
import com.farhad.sparkeditableprofile.domain.model.RequestStatus
import com.farhad.sparkeditableprofile.domain.usecase.repository.ProfileRepository
import io.reactivex.Observable

class ProfileRepositoryImpl(
    private val remote: Remote,
    private val requestStatusEntityMapper: RequestStatusEntityMapper,
    private val profileEntityMapper: ProfileEntityMapper
): ProfileRepository {
    override fun registerProfile(profile: Profile): Observable<RequestStatus> {
        return remote.registerProfile(profileEntityMapper.mapToData(profile))
            .map { requestStatusEntityMapper.mapToDomain(it) }
    }
}