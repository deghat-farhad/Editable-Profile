package com.farhad.sparkeditableprofile.di


import com.farhad.sparkeditableprofile.updateProfile.view.FragUpdateProfile
import com.farhad.sparkeditableprofile.viewProfile.view.FragViewProfile
import dagger.Component

@Component(modules = [ViewModelModule::class, DataModule::class, DomainModule::class])
interface ViewModelComponent {
    fun injectFragment(fragUpdateProfile: FragUpdateProfile)
    fun injectFragment(fragViewProfile: FragViewProfile)
}