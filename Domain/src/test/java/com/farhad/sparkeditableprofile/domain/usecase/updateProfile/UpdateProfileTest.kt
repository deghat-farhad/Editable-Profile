package com.farhad.sparkeditableprofile.domain.usecase.updateProfile

import com.farhad.sparkeditableprofile.domain.model.Profile
import com.farhad.sparkeditableprofile.domain.repository.ProfileRepository
import io.reactivex.Scheduler
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class UpdateProfileTest {

    @Mock
    lateinit var scheduler: Scheduler

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun buildUseCaseObservable() {
        val profileRepository = Mockito.mock(ProfileRepository::class.java)
        val profile = Mockito.mock(Profile::class.java)
        val updateProfile = UpdateProfile(scheduler,scheduler, profileRepository)
        val updateProfileParams = UpdateProfileParams(profile)

        updateProfile.buildUseCaseObservable(updateProfileParams)

        Mockito.verify(profileRepository).updateProfile(profile)
    }
}
