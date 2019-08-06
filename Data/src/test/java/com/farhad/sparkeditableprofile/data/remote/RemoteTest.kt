package com.farhad.sparkeditableprofile.data.remote

import com.farhad.sparkeditableprofile.data.entity.ProfileEntity
import com.farhad.sparkeditableprofile.data.entity.ProfilePictureEntity
import com.farhad.sparkeditableprofile.data.entity.RequestStatusEntity
import com.farhad.sparkeditableprofile.data.remote.services.ProfileService
import io.reactivex.Observable
import junit.framework.Assert.assertEquals
import net.bytebuddy.utility.RandomString
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.Before
import org.junit.Test
import org.mockito.*
import java.io.File

class RemoteTest {
    @Mock lateinit var serviceGenerator: ServiceGenerator
    @Mock lateinit var profileService: ProfileService

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(serviceGenerator.profileService()).thenReturn(profileService)
    }

    @Test
    fun registerProfile() {
        val profileEntity = ProfileEntity(id = RandomString.make(), answers = mapOf())
        val requestStatusEntity = RequestStatusEntity(null, null)

        Mockito.`when`(profileService.registerProfile(any())).thenReturn(Observable.just(requestStatusEntity))

        val remote = Remote(serviceGenerator)
        val registerProfileObservable = remote.registerProfile(profileEntity)

        registerProfileObservable.test().assertValue {
            it == requestStatusEntity
        }

        Mockito.verify(serviceGenerator).profileService()
        Mockito.verify(profileService).registerProfile(profileEntity)
    }

    @Test
    fun uploadProfilePicture(){
        val profilePictureEntity = ProfilePictureEntity(RandomString.make())
        val profileId = RandomString.make()
        val picture = File("picture.jpg")
        picture.writeBytes(byteArrayOf(0,1,2,3,4,5,6,7,8,9))

        Mockito.`when`(profileService.uploadProfilePicture(any()))
            .thenReturn(Observable.just(profilePictureEntity))

        val remote = Remote(serviceGenerator)
        val profilePictureObserver = remote.uploadProfilePicture(picture, profileId)

        profilePictureObserver.test().assertValue {
            it == profilePictureEntity
        }.onComplete()

        val multipartBodyPartCaptor: ArgumentCaptor<MultipartBody.Part>
                = ArgumentCaptor.forClass(MultipartBody.Part::class.java)

        Mockito.verify(serviceGenerator).profileService()
        Mockito.verify(profileService).uploadProfilePicture(
            capture(multipartBodyPartCaptor)
        )
        assertEquals(10, multipartBodyPartCaptor.value.body().contentLength())
    }

    fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }
    private fun <T> uninitialized(): T = null as T
}