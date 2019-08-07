package com.farhad.sparkeditableprofile.data.remote

import com.farhad.sparkeditableprofile.data.remote.services.ProfileService
import com.farhad.sparkeditableprofile.data.remote.services.QuestionService
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit

class ServiceGeneratorTest {
    @Mock
    lateinit var retrofit: Retrofit
    @Mock
    lateinit var profileService: ProfileService
    @Mock
    lateinit var questionService: QuestionService

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun profileService() {
        Mockito.`when`(retrofit.create(ProfileService::class.java)).thenReturn(profileService)
        val serviceGenerator = ServiceGenerator(retrofit)
        val generatedProfileService = serviceGenerator.profileService()

        assertEquals(profileService, generatedProfileService)
    }

    @Test
    fun questionService() {
        Mockito.`when`(retrofit.create(QuestionService::class.java)).thenReturn(questionService)
        val serviceGenerator = ServiceGenerator(retrofit)
        val generateQuestionService = serviceGenerator.questionService()

        assertEquals(generateQuestionService, questionService)
    }
}