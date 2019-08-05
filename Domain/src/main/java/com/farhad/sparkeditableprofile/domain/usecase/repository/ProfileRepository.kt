package com.farhad.sparkeditableprofile.domain.usecase.repository

import com.farhad.sparkeditableprofile.domain.model.Profile
import com.farhad.sparkeditableprofile.domain.model.RequestStatus
import io.reactivex.Observable

interface ProfileRepository {
    abstract fun registerProfile(profile: Profile): Observable<RequestStatus>
}