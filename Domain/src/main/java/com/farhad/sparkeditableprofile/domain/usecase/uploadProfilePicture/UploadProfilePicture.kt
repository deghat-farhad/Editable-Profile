package com.farhad.sparkeditableprofile.domain.usecase.uploadProfilePicture

import com.farhad.sparkeditableprofile.domain.model.ProfilePicture
import com.farhad.sparkeditableprofile.domain.usecase.base.UseCase
import com.farhad.sparkeditableprofile.domain.repository.ProfileRepository
import io.reactivex.Observable
import io.reactivex.Scheduler

class UploadProfilePicture(
    executorThread: Scheduler,
    uiThread: Scheduler,
    private val profileRepository: ProfileRepository
): UseCase<ProfilePicture, UploadProfilePictureParams>(executorThread, uiThread) {
    override fun buildUseCaseObservable(params: UploadProfilePictureParams): Observable<ProfilePicture> {
        return profileRepository.uploadProfilePicture(params.picture, params.profile)
    }
}