package com.farhad.sparkeditableprofile.domain.usecase.getProfile

import com.farhad.sparkeditableprofile.domain.repository.ProfileRepository
import io.reactivex.Scheduler
import net.bytebuddy.utility.RandomString
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GetProfileTest {
    @Mock
    lateinit var scheduler: Scheduler
    @Mock
    lateinit var profileRepository: ProfileRepository


    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun buildUseCaseObservable() {
        val id = RandomString().nextString()
        val getProfileParams = GetProfileParams(id)
        val getProfile = GetProfile(scheduler, scheduler, profileRepository)
        getProfile.buildUseCaseObservable(getProfileParams)


        Mockito.verify(profileRepository).getProfile(id)
    }
}