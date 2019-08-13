package com.farhad.sparkeditableprofile.domain.usecase.updateProfile

import com.farhad.sparkeditableprofile.domain.model.RequestStatus
import com.farhad.sparkeditableprofile.domain.repository.ProfileRepository
import com.farhad.sparkeditableprofile.domain.usecase.base.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

class UpdateProfile(
    executorThread: Scheduler,
    uiThread: Scheduler,
    private val profileRepository: ProfileRepository
) : UseCase<RequestStatus, UpdateProfileParams>(executorThread, uiThread) {

    override fun buildUseCaseObservable(params: UpdateProfileParams): Observable<RequestStatus> {
        return profileRepository.updateProfile(params.profile)
    }
}