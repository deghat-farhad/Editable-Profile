package com.farhad.sparkeditableprofile.domain.repository

import com.farhad.sparkeditableprofile.domain.model.Profile
import com.farhad.sparkeditableprofile.domain.model.ProfilePicture
import com.farhad.sparkeditableprofile.domain.model.RequestStatus
import io.reactivex.Observable
import java.io.File

interface ProfileRepository {
    fun registerProfile(profile: Profile): Observable<RequestStatus>
    fun uploadProfilePicture(picture: File, profile: Profile): Observable<ProfilePicture>
    fun getProfile(userId: String): Observable<Profile>
}