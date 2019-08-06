package com.farhad.sparkeditableprofile.data.mapper

import com.farhad.sparkeditableprofile.data.entity.LocationEntity
import com.farhad.sparkeditableprofile.data.entity.ProfilePictureEntity
import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.domain.model.Profile
import com.farhad.sparkeditableprofile.domain.model.ProfilePicture
import net.bytebuddy.utility.RandomString
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*
import kotlin.random.Random

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
        val profilePicture = ProfilePicture(RandomString.make())
        val location = Location(
            RandomString.make(),
            RandomString.make(),
            RandomString.make()
        )

        val profile = Profile(
            RandomString.make(),
            RandomString.make(),
            RandomString.make(),
            profilePicture,
            Date(0),
            Random.nextInt(),
            RandomString.make(),
            RandomString.make(),
            location,
            hashMapOf()
        )


        profile.location?.let {
            Mockito.`when`(locationEntityMapper.mapToData(it))
                .thenReturn(LocationEntity(location.lat, location.lon, location.city))
        }

        profile.profilePicture?.let {
            Mockito.`when`(profilePictureEntityMapper.mapToData(it))
                .thenReturn(ProfilePictureEntity(profilePicture.url))
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
}