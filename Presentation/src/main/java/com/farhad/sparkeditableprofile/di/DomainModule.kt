package com.farhad.sparkeditableprofile.di

import com.farhad.sparkeditableprofile.domain.repository.ProfileRepository
import com.farhad.sparkeditableprofile.domain.repository.QuestionRepository
import com.farhad.sparkeditableprofile.domain.usecase.getLocations.GetLocations
import com.farhad.sparkeditableprofile.domain.usecase.getProfile.GetProfile
import com.farhad.sparkeditableprofile.domain.usecase.getSingleChoiceAnswers.GetSingleChoiceAnswers
import com.farhad.sparkeditableprofile.domain.usecase.registerProfile.RegisterProfile
import com.farhad.sparkeditableprofile.domain.usecase.uploadProfilePicture.UploadProfilePicture
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

@Module
class DomainModule {

    @Provides
    fun getProfile(
        profileRepository: ProfileRepository,
        @Named("ioScheduler") ioScheduler: Scheduler,
        @Named("mainThreadScheduler") mainThreadScheduler: Scheduler
    ): GetProfile {
        return GetProfile(ioScheduler, mainThreadScheduler, profileRepository)
    }

    @Provides
    fun getSingleChoiceAnswers(
        questionsRepository: QuestionRepository,
        @Named("ioScheduler") ioScheduler: Scheduler,
        @Named("mainThreadScheduler") mainThreadScheduler: Scheduler
    ): GetSingleChoiceAnswers {
        return GetSingleChoiceAnswers(ioScheduler, mainThreadScheduler, questionsRepository)
    }

    @Provides
    fun getLocations(
        questionsRepository: QuestionRepository,
        @Named("ioScheduler") ioScheduler: Scheduler,
        @Named("mainThreadScheduler") mainThreadScheduler: Scheduler
    ): GetLocations {
        return GetLocations(ioScheduler, mainThreadScheduler, questionsRepository)
    }

    @Provides
    fun uploadPicture(
        profileRepository: ProfileRepository,
        @Named("ioScheduler") ioScheduler: Scheduler,
        @Named("mainThreadScheduler") mainThreadScheduler: Scheduler
    ): UploadProfilePicture {
        return UploadProfilePicture(ioScheduler, mainThreadScheduler, profileRepository)
    }

    @Provides
    fun registerProfile(
        profileRepository: ProfileRepository,
        @Named("ioScheduler") ioScheduler: Scheduler,
        @Named("mainThreadScheduler") mainThreadScheduler: Scheduler
    ): RegisterProfile {
        return RegisterProfile(ioScheduler, mainThreadScheduler, profileRepository)
    }

    @Provides
    @Named("ioScheduler")
    fun ioScheduler(): Scheduler = Schedulers.io()

    @Provides
    @Named("mainThreadScheduler")
    fun mainThreadScheduler(): Scheduler = AndroidSchedulers.mainThread()
}