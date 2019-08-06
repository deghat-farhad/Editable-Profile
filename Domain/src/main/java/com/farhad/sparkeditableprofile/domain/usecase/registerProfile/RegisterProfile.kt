package com.farhad.sparkeditableprofile.domain.usecase.registerProfile

import com.farhad.sparkeditableprofile.domain.model.RequestStatus
import com.farhad.sparkeditableprofile.domain.repository.ProfileRepository
import com.farhad.sparkeditableprofile.domain.usecase.base.UseCase
import io.reactivex.Observable
import io.reactivex.Scheduler

class RegisterProfile(
    executorThread: Scheduler,
    uiThread: Scheduler,
    private val profileRepository: ProfileRepository
): UseCase<RequestStatus, RegisterParams>(executorThread, uiThread) {
    override fun buildUseCaseObservable(params: RegisterParams): Observable<RequestStatus> {
        return profileRepository.registerProfile(params.profile)
    }
}