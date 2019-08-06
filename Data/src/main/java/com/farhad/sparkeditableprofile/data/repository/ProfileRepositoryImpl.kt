package com.farhad.sparkeditableprofile.data.repository

import com.farhad.sparkeditableprofile.data.mapper.ProfileEntityMapper
import com.farhad.sparkeditableprofile.data.mapper.RequestStatusEntityMapper
import com.farhad.sparkeditableprofile.data.remote.Remote
import com.farhad.sparkeditableprofile.domain.model.Profile
import com.farhad.sparkeditableprofile.domain.model.ProfilePicture
import com.farhad.sparkeditableprofile.domain.model.RequestStatus
import com.farhad.sparkeditableprofile.domain.usecase.repository.ProfileRepository
import io.reactivex.Observable
import java.io.File

class ProfileRepositoryImpl(
    private val remote: Remote,
    private val requestStatusEntityMapper: RequestStatusEntityMapper,
    private val profileEntityMapper: ProfileEntityMapper
): ProfileRepository {
    override fun uploadProfilePicture(picture: File): Observable<ProfilePicture> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun registerProfile(profile: Profile): Observable<RequestStatus> {
        return remote.registerProfile(profileEntityMapper.mapToData(profile))
            .map { requestStatusEntityMapper.mapToDomain(it) }
    }
}