package com.farhad.sparkeditableprofile.data.mapper

import com.farhad.sparkeditableprofile.data.entity.LocationEntity
import com.farhad.sparkeditableprofile.data.entity.ProfilePictureEntity
import com.farhad.sparkeditableprofile.data.testUtils.FakeProfile
import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.domain.model.ProfilePicture
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ProfileEntityMapperTest {


    @Mock
    lateinit var locationEntityMapper: LocationEntityMapper
    @Mock
    lateinit var singleChoiceAnswerEntityMapper: SingleChoiceAnswerEntityMapper
    @Mock
    lateinit var profilePictureEntityMapper: ProfilePictureEntityMapper

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun mapToData() {
        val profile = FakeProfile().getProfile()
        profile.location?.let {
            Mockito.`when`(locationEntityMapper.mapToData(it))
                .thenReturn(LocationEntity(it.lat, it.lon, it.city))
        }

        profile.profilePicture?.let {
            Mockito.`when`(profilePictureEntityMapper.mapToData(it))
                .thenReturn(ProfilePictureEntity(it.url))
        }

        Mockito.`when`(singleChoiceAnswerEntityMapper.mapToData(profile.answers)).thenReturn(hashMapOf())

        val profileEntityMapper = ProfileEntityMapper(
            locationEntityMapper,
            singleChoiceAnswerEntityMapper,
            profilePictureEntityMapper
        )

        val profileEntity = profileEntityMapper.mapToData(profile)

        assertEquals(profileEntity.id, profile.id)
        assertEquals(profileEntity.Display_Name, profile.displayName)
        assertEquals(profileEntity.Real_Name, profile.realName)
        assertEquals(profileEntity.Birthday, profile.birthday)
        assertEquals(profileEntity.Height, profile.height)
        assertEquals(profileEntity.Occupation, profile.occupation)
        assertEquals(profileEntity.About_Me, profile.aboutMe)
        assertEquals(profileEntity.location?.lat, profile.location?.lat)
        assertEquals(profileEntity.location?.lon, profile.location?.lon)
        assertEquals(profileEntity.location?.city, profile.location?.city)
        assertEquals(profileEntity.Profile_Picture?.url, profile.profilePicture?.url)
    }

    @Test
    fun mapToDomain() {
        val profileEntity = FakeProfile().getProfileEntity()
        profileEntity.location?.let {
            Mockito.`when`(locationEntityMapper.mapToDomain(it))
                .thenReturn(Location(it.lat, it.lon, it.city))
        }

        profileEntity.Profile_Picture?.let {
            Mockito.`when`(profilePictureEntityMapper.mapToDomain(it))
                .thenReturn(ProfilePicture(it.url))
        }

        Mockito.`when`(singleChoiceAnswerEntityMapper.mapToDomain(profileEntity.answers)).thenReturn(hashMapOf())

        val profileMapper = ProfileEntityMapper(
            locationEntityMapper,
            singleChoiceAnswerEntityMapper,
            profilePictureEntityMapper
        )

        val profile = profileMapper.mapToDomain(profileEntity)

        assertEquals(profile.id, profileEntity.id)
        assertEquals(profile.displayName, profileEntity.Display_Name)
        assertEquals(profile.realName, profileEntity.Real_Name)
        assertEquals(profile.birthday, profileEntity.Birthday)
        assertEquals(profile.height, profileEntity.Height)
        assertEquals(profile.occupation, profileEntity.Occupation)
        assertEquals(profile.aboutMe, profileEntity.About_Me)
        assertEquals(profile.location?.lat, profileEntity.location?.lat)
        assertEquals(profile.location?.lon, profileEntity.location?.lon)
        assertEquals(profile.location?.city, profileEntity.location?.city)
        assertEquals(profile.profilePicture?.url, profileEntity.Profile_Picture?.url)
    }
}