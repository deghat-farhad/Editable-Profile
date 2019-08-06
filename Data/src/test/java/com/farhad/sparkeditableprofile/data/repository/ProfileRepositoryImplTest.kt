package com.farhad.sparkeditableprofile.data.repository

import com.farhad.sparkeditableprofile.data.entity.ProfileEntity
import com.farhad.sparkeditableprofile.data.entity.ProfilePictureEntity
import com.farhad.sparkeditableprofile.data.entity.RequestStatusEntity
import com.farhad.sparkeditableprofile.data.mapper.ProfileEntityMapper
import com.farhad.sparkeditableprofile.data.mapper.ProfilePictureEntityMapper
import com.farhad.sparkeditableprofile.data.mapper.RequestStatusEntityMapper
import com.farhad.sparkeditableprofile.data.remote.Remote
import com.farhad.sparkeditableprofile.domain.model.Profile
import com.farhad.sparkeditableprofile.domain.model.ProfilePicture
import com.farhad.sparkeditableprofile.domain.model.RequestStatus
import io.reactivex.Observable
import net.bytebuddy.utility.RandomString
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
        val randomMessage = RandomString.make()
        val requestStatusEntity = RequestStatusEntity(randomSuccess, randomMessage)
        val requestStatus = RequestStatus(randomSuccess, randomMessage)
        val profileEntity = ProfileEntity(
            id = RandomString.make(),
            answers = hashMapOf()
        )
        val profile = Profile(
            id = RandomString.make(),
            answers = hashMapOf()
        )

        Mockito.`when`(remote.registerProfile(any())).thenReturn(Observable.just(requestStatusEntity))
        Mockito.`when`(requestStatusEntityMapper.mapToDomain(any())).thenReturn(requestStatus)
        Mockito.`when`(profileEntityMapper.mapToData(any())).thenReturn(profileEntity)

        val profileRepositoryImpl = ProfileRepositoryImpl(
            remote,
            requestStatusEntityMapper,
            profileEntityMapper,
            profilePictureEntityMapper
        )

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
        val profile = Profile(
            id = RandomString.make(),
            answers = hashMapOf()
        )
        val profilePictureEntity = ProfilePictureEntity(RandomString.make())
        val profilePicture = ProfilePicture(profilePictureEntity.url)

        Mockito.`when`(remote.uploadProfilePicture(any(), any())).thenReturn(Observable.just(profilePictureEntity))
        Mockito.`when`(profilePictureEntityMapper.mapToDomain(any())).thenReturn(profilePicture)

        val profileRepositoryImpl = ProfileRepositoryImpl(
            remote,
            requestStatusEntityMapper,
            profileEntityMapper,
            profilePictureEntityMapper
        )

        val profilePictureObservable
                = profileRepositoryImpl.uploadProfilePicture(picture, profile)

        profilePictureObservable.test().assertValue {
            it == profilePicture
        }.onComplete()

        Mockito.verify(remote).uploadProfilePicture(picture, profile.id)
        Mockito.verify(profilePictureEntityMapper).mapToDomain(profilePictureEntity)
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}