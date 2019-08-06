package com.farhad.sparkeditableprofile.data.mapper

import com.farhad.sparkeditableprofile.data.entity.ProfilePictureEntity
import com.farhad.sparkeditableprofile.domain.model.ProfilePicture
import net.bytebuddy.utility.RandomString
import org.junit.Test

import org.junit.Assert.*

class ProfilePictureEntityMapperTest {

    @Test
    fun mapToDomain() {
        val profilePictureEntity =  ProfilePictureEntity(
        RandomString.make()
        )

        val profilePictureEntityMapper = ProfilePictureEntityMapper()

        val profilePicture = profilePictureEntityMapper.mapToDomain(profilePictureEntity)

        assertEquals(profilePicture.url, profilePictureEntity.url)
    }

    @Test
    fun mapToData() {
        val profilePicture =  ProfilePicture(
            RandomString.make()
        )

        val profilePictureEntityMapper = ProfilePictureEntityMapper()

        val profilePictureEntity = profilePictureEntityMapper.mapToData(profilePicture)

        assertEquals(profilePictureEntity.url, profilePicture.url)
    }
}