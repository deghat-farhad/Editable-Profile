package com.farhad.sparkeditableprofile.data.remote

import com.farhad.sparkeditableprofile.data.entity.ProfileEntity
import com.farhad.sparkeditableprofile.data.entity.RequestStatusEntity
import com.farhad.sparkeditableprofile.data.remote.Services.ProfileService
import io.reactivex.Observable
import net.bytebuddy.utility.RandomString
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class RemoteTest {
    @Mock lateinit var serviceGenerator: ServiceGenerator
    @Mock lateinit var profileService: ProfileService

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun registerProfile() {
        val profileEntity = ProfileEntity(id = RandomString.make(), answers = mapOf())
        val requestStatusEntity = RequestStatusEntity(null, null)

        Mockito.`when`(serviceGenerator.profileService()).thenReturn(profileService)
        Mockito.`when`(profileService.registerProfile(any())).thenReturn(Observable.just(requestStatusEntity))

        val remote = Remote(serviceGenerator)
        val registerProfileObservable = remote.registerProfile(profileEntity)

        registerProfileObservable.test().assertValue {
            it == requestStatusEntity
        }

        Mockito.verify(serviceGenerator).profileService()
        Mockito.verify(profileService).registerProfile(profileEntity)
    }
    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }
    private fun <T> uninitialized(): T = null as T
}