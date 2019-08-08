package com.farhad.sparkeditableprofile.di


import com.farhad.sparkeditableprofile.updateProfile.view.FragUpdateProfile
import dagger.Component

@Component(modules = [ViewModelModule::class, DataModule::class, DomainModule::class])
interface ViewModelComponent {
    fun injectFragment(fragUpdateProfile: FragUpdateProfile)
}