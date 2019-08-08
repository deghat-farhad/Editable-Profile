package com.farhad.sparkeditableprofile.di

import com.farhad.sparkeditableprofile.domain.repository.QuestionRepository
import com.farhad.sparkeditableprofile.domain.usecase.getSingleChoiceAnswers.GetSingleChoiceAnswers
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class DomainModule {
    @Provides fun getSingleChoiceAnswers(questionsRepository: QuestionRepository): GetSingleChoiceAnswers{
        return GetSingleChoiceAnswers(Schedulers.io(), AndroidSchedulers.mainThread(), questionsRepository)
    }
}