package com.farhad.sparkeditableprofile.data.remote

import com.farhad.sparkeditableprofile.data.remote.Services.ProfileService
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit

class ServiceGeneratorTest {
    @Mock lateinit var retrofit: Retrofit
    @Mock lateinit var profileService: ProfileService

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun profileService() {
        Mockito.`when`(retrofit.create(ProfileService::class.java)).thenReturn(profileService)
        val serviceGenerator = ServiceGenerator(retrofit)
        val generatedProfileService = serviceGenerator.profileService()

        assertEquals(profileService, generatedProfileService)
    }
}