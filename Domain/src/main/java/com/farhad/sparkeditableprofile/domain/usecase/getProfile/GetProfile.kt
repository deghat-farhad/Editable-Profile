package com.farhad.sparkeditableprofile.domain.usecase.getProfile

import com.farhad.sparkeditableprofile.domain.model.Profile
import com.farhad.sparkeditableprofile.domain.repository.ProfileRepository
import com.farhad.sparkeditableprofile.domain.usecase.base.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

class GetProfile(
    executorThread: Scheduler,
    uiThread: Scheduler,
    private val profileRepository: ProfileRepository
) : UseCase<Profile, GetProfileParams>(executorThread, uiThread) {

    override fun buildUseCaseObservable(params: GetProfileParams): Observable<Profile> {
        return profileRepository.getProfile(params.userId)
    }
}