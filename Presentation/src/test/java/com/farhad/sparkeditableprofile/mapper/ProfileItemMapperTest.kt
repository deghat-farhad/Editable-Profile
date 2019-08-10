package com.farhad.sparkeditableprofile.mapper

import android.provider.ContactsContract
import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.domain.model.Profile
import com.farhad.sparkeditableprofile.model.ProfileItem
import com.farhad.sparkeditableprofile.testUtils.FakeLocations
import com.farhad.sparkeditableprofile.testUtils.FakeProfile
import com.farhad.sparkeditableprofile.testUtils.FakeSingleChoices
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ProfileItemMapperTest {
    lateinit var profile: Profile
    lateinit var profileItem: ProfileItem
    lateinit var profileItemMapper: ProfileItemMapper

    @Mock
    lateinit var locationItemMapper: LocationItemMapper
    @Mock
    lateinit var singleChoiceAnswerItemMapper: SingleChoiceAnswerItemMapper
    @Mock
    lateinit var profilePictureItemMapper: ProfilePictureItemMapper

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)

        val locations = FakeLocations().generateLocationItemList(100)
        val answers = FakeSingleChoices().generateFakeSingleChoiceAnswerItemListMap(10, 8)
        val fakeProfile = FakeProfile(locations, answers)

        profile = fakeProfile.getProfile()
        profileItem = fakeProfile.getProfileItem()

        Mockito.`when`(locationItemMapper.mapToPresentation(any<Location>())).thenReturn(profileItem.location)
        Mockito.`when`(locationItemMapper.mapToDomain(any())).thenReturn(profile.location)

        Mockito.`when`(singleChoiceAnswerItemMapper.mapToPresentation(any())).thenReturn(profileItem.answers)
        Mockito.`when`(singleChoiceAnswerItemMapper.mapToDomain(any())).thenReturn(profile.answers)

        Mockito.`when`(profilePictureItemMapper.mapToPresentation(any())).thenReturn(profileItem.profilePicture)
        Mockito.`when`(profilePictureItemMapper.mapToDomain(any())).thenReturn(profile.profilePicture)

        profileItemMapper = ProfileItemMapper(locationItemMapper, singleChoiceAnswerItemMapper, profilePictureItemMapper)
    }

    @Test
    fun mapToPresentation() {
        val profileItem = profileItemMapper.mapToPresentation(profile)

        assertEquals(profile.id, profileItem.id)
        assertEquals(profile.displayName, profileItem.displayName)
        assertEquals(profile.realName, profileItem.realName)
        assertEquals(profile.birthday, profileItem.birthday)
        assertEquals(profile.height, profileItem.height)
        assertEquals(profile.occupation, profileItem.occupation)
        assertEquals(profile.aboutMe, profileItem.aboutMe)

        assertEquals(profile.location?.lon, profileItem.location?.lon)
        assertEquals(profile.location?.lat, profileItem.location?.lat)
        assertEquals(profile.location?.city, profileItem.location?.city)

        for(question in profile.answers.keys){
            assertEquals(profile.answers[question]?.id, profileItem.answers[question]?.id)
            assertEquals(profile.answers[question]?.name, profileItem.answers[question]?.name)
        }

        assertEquals(profile.profilePicture?.url, profileItem.profilePicture?.url)
    }

    @Test
    fun mapToDomain() {
        val profile = profileItemMapper.mapToDomain(profileItem)

        assertEquals(profileItem.id, profile.id)
        assertEquals(profileItem.displayName, profile.displayName)
        assertEquals(profileItem.realName, profile.realName)
        assertEquals(profileItem.birthday, profile.birthday)
        assertEquals(profileItem.height, profile.height)
        assertEquals(profileItem.occupation, profile.occupation)
        assertEquals(profileItem.aboutMe, profile.aboutMe)

        assertEquals(profileItem.location?.lon, profile.location?.lon)
        assertEquals(profileItem.location?.lat, profile.location?.lat)
        assertEquals(profileItem.location?.city, profile.location?.city)

        for(question in profileItem.answers.keys){
            assertEquals(profileItem.answers[question]?.id, profile.answers[question]?.id)
            assertEquals(profileItem.answers[question]?.name, profile.answers[question]?.name)
        }

        assertEquals(profileItem.profilePicture?.url, profile.profilePicture?.url)
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}