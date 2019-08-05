package com.farhad.sparkeditableprofile.data.di

import com.farhad.sparkeditableprofile.data.repository.ProfileRepositoryImpl
import dagger.Component

@Component(modules = [RepositoriesModule::class])
interface DataComponent {
    fun getProfileRepositoryImpl(): ProfileRepositoryImpl
}