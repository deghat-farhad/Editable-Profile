package com.farhad.sparkeditableprofile.data.repository

import com.farhad.sparkeditableprofile.data.entity.ProfilePictureEntity
import com.farhad.sparkeditableprofile.data.entity.RequestStatusEntity
import com.farhad.sparkeditableprofile.data.mapper.ProfileEntityMapper
import com.farhad.sparkeditableprofile.data.mapper.ProfilePictureEntityMapper
import com.farhad.sparkeditableprofile.data.mapper.RequestStatusEntityMapper
import com.farhad.sparkeditableprofile.data.remote.Remote
import com.farhad.sparkeditableprofile.data.testUtils.FakeProfile
import com.farhad.sparkeditableprofile.data.testUtils.RandomString
import com.farhad.sparkeditableprofile.domain.model.ProfilePicture
import com.farhad.sparkeditableprofile.domain.model.RequestStatus
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.io.File

class ProfileRepositoryImplTest {

    @Mock
    lateinit var remote: Remote
    @Mock
    lateinit var requestStatusEntityMapper: RequestStatusEntityMapper
    @Mock
    lateinit var profileEntityMapper: ProfileEntityMapper
    @Mock
    lateinit var profilePictureEntityMapper: ProfilePictureEntityMapper
    @Mock
    lateinit var picture: File

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun registerProfile() {
        val randomSuccess = java.util.Random().nextBoolean()
        val randomMessage = RandomString().get()
        val requestStatusEntity = RequestStatusEntity(randomSuccess, randomMessage)
        val requestStatus = RequestStatus(randomSuccess, randomMessage)
        val fakeProfile = FakeProfile()
        val profileEntity = fakeProfile.getProfileEntity()
        val profile = fakeProfile.getProfile()

        Mockito.`when`(remote.registerProfile(any())).thenReturn(Observable.just(requestStatusEntity))
        Mockito.`when`(requestStatusEntityMapper.mapToDomain(any())).thenReturn(requestStatus)
        Mockito.`when`(profileEntityMapper.mapToData(any())).thenReturn(profileEntity)

        val profileRepositoryImpl = getProfileRepositoryImpl()

        val requestStatusObservable = profileRepositoryImpl.registerProfile(profile)

        requestStatusObservable.test().assertValue {
            it.success == requestStatus.success &&
                    it.message == requestStatus.message
        }.onComplete()

        Mockito.verify(remote).registerProfile(profileEntity)
        Mockito.verify(profileEntityMapper).mapToData(profile)
        Mockito.verify(requestStatusEntityMapper).mapToDomain(requestStatusEntity)
    }

    @Test
    fun uploadProfilePicture() {
        val profile = FakeProfile().getProfile()
        val profilePictureEntity = ProfilePictureEntity(RandomString().get())
        val profilePicture = ProfilePicture(profilePictureEntity.url)

        Mockito.`when`(remote.uploadProfilePicture(any(), any())).thenReturn(Observable.just(profilePictureEntity))
        Mockito.`when`(profilePictureEntityMapper.mapToDomain(any())).thenReturn(profilePicture)

        val profileRepositoryImpl = getProfileRepositoryImpl()

        val profilePictureObservable
                = profileRepositoryImpl.uploadProfilePicture(picture, profile)

        profilePictureObservable.test().assertValue {
            it == profilePicture
        }.onComplete()

        Mockito.verify(remote).uploadProfilePicture(picture, profile.id)
        Mockito.verify(profilePictureEntityMapper).mapToDomain(profilePictureEntity)
    }

    @Test
    fun getProfile(){
        val id = RandomString().get()
        val fakeProfile = FakeProfile()
        val profile = fakeProfile.getProfile()
        val profileEntity = fakeProfile.getProfileEntity()

        Mockito.`when`(profileEntityMapper.mapToDomain(profileEntity)).thenReturn(profile)
        Mockito.`when`(remote.getProfile(id)).thenReturn(Observable.just(profileEntity))

        val profileRepositoryImpl = getProfileRepositoryImpl()

        val profileObservable = profileRepositoryImpl.getProfile(id)

        Mockito.verify(remote).getProfile(id)

        profileObservable.test().assertValue {
            it == profile
        }.onComplete()
    }

    private fun getProfileRepositoryImpl(): ProfileRepositoryImpl {
        return ProfileRepositoryImpl(
            remote,
            requestStatusEntityMapper,
            profileEntityMapper,
            profilePictureEntityMapper
        )
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}