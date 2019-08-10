package com.farhad.sparkeditableprofile.mapper

import com.farhad.sparkeditableprofile.domain.model.ProfilePicture
import com.farhad.sparkeditableprofile.model.ProfilePictureItem
import com.farhad.sparkeditableprofile.testUtils.RandomString
import org.junit.Assert.*
import org.junit.Test

class ProfilePictureItemMapperTest{
    @Test
    fun mapToPresentation(){
        val profilePicture = ProfilePicture(RandomString().get())
        val profilePictureItem = ProfilePictureItemMapper().mapToPresentation(profilePicture)

        assertEquals(profilePicture.url, profilePictureItem.url)
    }
    @Test
    fun mapToDomain(){
        val profilePictureItem = ProfilePictureItem(RandomString().get())
        val profilePicture = ProfilePictureItemMapper().mapToDomain(profilePictureItem)

        assertEquals(profilePictureItem.url, profilePicture.url)
    }
}