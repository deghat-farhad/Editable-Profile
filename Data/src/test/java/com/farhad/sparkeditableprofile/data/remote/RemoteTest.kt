package com.farhad.sparkeditableprofile.data.remote

import com.farhad.sparkeditableprofile.data.entity.*
import com.farhad.sparkeditableprofile.data.remote.services.ProfileService
import com.farhad.sparkeditableprofile.data.remote.services.QuestionService
import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer
import io.reactivex.Observable
import junit.framework.Assert.assertEquals
import net.bytebuddy.utility.RandomString
import okhttp3.MultipartBody
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.io.File

class RemoteTest {
    @Mock
    lateinit var serviceGenerator: ServiceGenerator
    @Mock
    lateinit var profileService: ProfileService
    @Mock
    lateinit var questionService: QuestionService

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(serviceGenerator.profileService()).thenReturn(profileService)
    }

    @Test
    fun registerProfile() {
        val profileEntity = ProfileEntity(id = RandomString.make(), answers = mapOf())
        val requestStatusEntity = RequestStatusEntity(null, null)

        Mockito.`when`(profileService.registerProfile(any())).thenReturn(Observable.just(requestStatusEntity))

        val remote = Remote(serviceGenerator)
        val registerProfileObservable = remote.registerProfile(profileEntity)

        registerProfileObservable.test().assertValue {
            it == requestStatusEntity
        }

        Mockito.verify(serviceGenerator).profileService()
        Mockito.verify(profileService).registerProfile(profileEntity)
    }

    @Test
    fun uploadProfilePicture() {
        val profilePictureEntity = ProfilePictureEntity(RandomString.make())
        val profileId = RandomString.make()
        val picture = File("picture.jpg")
        picture.writeBytes(byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9))

        Mockito.`when`(profileService.uploadProfilePicture(any()))
            .thenReturn(Observable.just(profilePictureEntity))

        val remote = Remote(serviceGenerator)
        val profilePictureObserver = remote.uploadProfilePicture(picture, profileId)

        profilePictureObserver.test().assertValue {
            it == profilePictureEntity
        }.onComplete()

        val multipartBodyPartCaptor: ArgumentCaptor<MultipartBody.Part> =
            ArgumentCaptor.forClass(MultipartBody.Part::class.java)

        Mockito.verify(serviceGenerator).profileService()
        Mockito.verify(profileService).uploadProfilePicture(
            capture(multipartBodyPartCaptor)
        )
        assertEquals(10, multipartBodyPartCaptor.value.body().contentLength())
    }

    @Test
    fun getLocations() {
        val locationEntityList = generateFakeLocationList(100)
        Mockito.`when`(serviceGenerator.questionService()).thenReturn(questionService)
        Mockito.`when`(questionService.getLocations()).thenReturn(Observable.just(locationEntityList))
        val remote = Remote(serviceGenerator)
        val observableLocationEntityList = remote.getLocations()

        observableLocationEntityList.test().assertValue {
            it == locationEntityList
        }.onComplete()
    }

    @Test
    fun getSingleChoiceAnswers(){
        val singleChoiceAnswerEntityMap
                = generateFakeSingleChoiceAnswerEntityMap(10, 10)
        Mockito.`when`(serviceGenerator.questionService()).thenReturn(questionService)
        Mockito.`when`(questionService.getSingleChoiceAnswers())
            .thenReturn(Observable.just(singleChoiceAnswerEntityMap))

        val remote = Remote(serviceGenerator)

        val observableSingleChoiceAnswerEntityMap
                = remote.getSingleChoiceAnswers()
        observableSingleChoiceAnswerEntityMap.test().assertValue {
            it == singleChoiceAnswerEntityMap
        }.onComplete()
    }

    private fun generateFakeSingleChoiceAnswerEntityMap(questionCnt: Int, answerCnt: Int): HashMap<String, List<SingleChoiceAnswerEntity>>{
        val output = HashMap<String, List<SingleChoiceAnswerEntity>>()

        for (questionCntr in (1 .. questionCnt)){
            val question = RandomString.make()
            val answerList = mutableListOf<SingleChoiceAnswerEntity>()
            for (answerCntr in (1 .. answerCnt)){
                answerList.add(
                    SingleChoiceAnswerEntity(RandomString.make(), RandomString.make())
                )
            }
            output[question] = answerList
        }

        return output
    }

    private fun generateFakeLocationList(count: Int): List<LocationEntity> {
        val output = mutableListOf<LocationEntity>()
        (1..count).map { output.add(generateFakeLocationEntity()) }
        return output
    }

    private fun generateFakeLocationEntity() =
        LocationEntity(RandomString.make(), RandomString.make(), RandomString.make())

    fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}