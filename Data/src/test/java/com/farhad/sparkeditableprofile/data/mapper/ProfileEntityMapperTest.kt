package com.farhad.sparkeditableprofile.data.mapper

import com.farhad.sparkeditableprofile.domain.model.Profile
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

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun mapToData() {

        val profile = Profile(
            RandomString.make(),
            RandomString.make(),
            RandomString.make(),
            RandomString.make(),
            Date(0),
            Random.nextInt(),
            RandomString.make(),
            RandomString.make(),
            null,
            hashMapOf()
        )


        profile.location?.let {
            Mockito.`when`(locationEntityMapper.mapToData(it)).thenReturn(null)
        }


        Mockito.`when`(singleChoiceAnswerEntityMapper.mapToData(profile.answers)).thenReturn(hashMapOf())

        val profileEntityMapper = ProfileEntityMapper(locationEntityMapper, singleChoiceAnswerEntityMapper)
        val profileEntity = profileEntityMapper.mapToData(profile)

        assertEquals(profileEntity.id, profile.id)
        assertEquals(profileEntity.Display_Name, profile.displayName)
        assertEquals(profileEntity.Real_Name, profile.realName)
        assertEquals(profileEntity.Profile_Picture, profile.profilePicture)
        assertEquals(profileEntity.Birthday, profile.birthday)
        assertEquals(profileEntity.Height, profile.height)
        assertEquals(profileEntity.Occupation, profile.occupation)
        assertEquals(profileEntity.About_Me, profile.aboutMe)
    }
}