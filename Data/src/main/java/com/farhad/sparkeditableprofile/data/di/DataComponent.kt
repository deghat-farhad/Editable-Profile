package com.farhad.sparkeditableprofile.data.di

import com.farhad.sparkeditableprofile.data.repository.ProfileRepositoryImpl
import com.farhad.sparkeditableprofile.data.repository.QuestionRepositoryImpl
import dagger.Component

@Component(modules = [RepositoriesModule::class])
interface DataComponent {
    fun getProfileRepositoryImpl(): ProfileRepositoryImpl
    fun getQuestionRepositoryImpl(): QuestionRepositoryImpl
}