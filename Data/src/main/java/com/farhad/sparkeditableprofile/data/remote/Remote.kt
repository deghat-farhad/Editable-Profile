package com.farhad.sparkeditableprofile.data.remote

import com.farhad.sparkeditableprofile.data.entity.ProfileEntity

class Remote(private val serviceGenerator: ServiceGenerator) {
    fun registerProfile(profileEntity: ProfileEntity) = serviceGenerator.profileService().registerProfile(profileEntity)
}
