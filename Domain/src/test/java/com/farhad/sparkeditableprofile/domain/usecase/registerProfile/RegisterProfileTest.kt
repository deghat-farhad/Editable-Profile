package com.farhad.sparkeditableprofile.domain.usecase.registerProfile

import com.farhad.sparkeditableprofile.domain.model.Profile
import com.farhad.sparkeditableprofile.domain.model.RequestStatus
import com.farhad.sparkeditableprofile.domain.usecase.repository.ProfileRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import org.junit.Test
import org.mockito.Mockito

class RegisterProfileTest {

    @Test
    fun testObservable() {

        val requestStatus = Mockito.mock(RequestStatus::class.java)
        val profileRepository = Mockito.mock(ProfileRepository::class.java)
        val Profile = Mockito.mock(Profile::class.java)
        Mockito.`when`(profileRepository.registerProfile(Profile)).thenReturn(
            Observable.just(requestStatus)
        )
        val subscribeOnScheduler = Mockito.mock(Scheduler::class.java)
        val observeOnScheduler = Mockito.mock(Scheduler::class.java)


        val registerProfile = RegisterProfile(subscribeOnScheduler, observeOnScheduler, profileRepository)
        val regesterParams = RegisterParams(Profile)

        registerProfile.buildUseCaseObservable(regesterParams).test().assertValue {
            it == requestStatus
        }.onComplete()
    }
}