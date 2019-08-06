package com.farhad.sparkeditableprofile.domain.usecase.uploadProfilePicture

import com.farhad.sparkeditableprofile.domain.model.Profile
import com.farhad.sparkeditableprofile.domain.model.ProfilePicture
import com.farhad.sparkeditableprofile.domain.repository.ProfileRepository
import io.reactivex.Observable
import io.reactivex.Scheduler
import net.bytebuddy.utility.RandomString
import org.junit.Test

import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.io.File

class UploadProfilePictureTest {
    @Mock lateinit var scheduler: Scheduler
    @Mock lateinit var profileRepository: ProfileRepository
    @Mock lateinit var picture: File

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun buildUseCaseObservable() {
        val profile = Profile(id = RandomString.make(), answers = hashMapOf())
        val profilePictureParams = UploadProfilePictureParams(picture, profile)
        val profilePicture = ProfilePicture(RandomString.make())
        val uploadProfilePicture = UploadProfilePicture(scheduler, scheduler, profileRepository)

        Mockito.`when`(profileRepository.uploadProfilePicture(any(), any())).thenReturn(Observable.just(profilePicture))

        val updateProfileObservable = uploadProfilePicture.buildUseCaseObservable(profilePictureParams)


        updateProfileObservable.test().assertValue {
            it == profilePicture
        }
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }
    private fun <T> uninitialized(): T = null as T
}