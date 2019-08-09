package com.farhad.sparkeditableprofile.di

import com.farhad.sparkeditableprofile.domain.repository.QuestionRepository
import com.farhad.sparkeditableprofile.domain.usecase.getLocations.GetLocations
import com.farhad.sparkeditableprofile.domain.usecase.getSingleChoiceAnswers.GetSingleChoiceAnswers
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

@Module
class DomainModule {
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
    @Named("ioScheduler")
    fun ioScheduler(): Scheduler = Schedulers.io()

    @Provides
    @Named("mainThreadScheduler")
    fun mainThreadScheduler(): Scheduler = AndroidSchedulers.mainThread()
}