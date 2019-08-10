package com.farhad.sparkeditableprofile.di

import com.farhad.sparkeditableprofile.data.di.DaggerDataComponent
import com.farhad.sparkeditableprofile.domain.repository.ProfileRepository
import com.farhad.sparkeditableprofile.domain.repository.QuestionRepository
import dagger.Module
import dagger.Provides

@Module
class DataModule {
    @Provides
    fun questionRepository(): QuestionRepository = DaggerDataComponent.create().getQuestionRepositoryImpl()

    @Provides
    fun profileRepository(): ProfileRepository = DaggerDataComponent.create().getProfileRepositoryImpl()
}