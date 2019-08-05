package com.farhad.sparkeditableprofile.data.remote

import com.farhad.sparkeditableprofile.data.remote.Services.ProfileService
import retrofit2.Retrofit

class ServiceGenerator(private val retrofit: Retrofit) {
    fun profileService(): ProfileService = retrofit.create(ProfileService::class.java)
}