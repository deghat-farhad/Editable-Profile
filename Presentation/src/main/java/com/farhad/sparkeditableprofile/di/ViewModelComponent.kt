package com.farhad.sparkeditableprofile.di


import com.farhad.sparkeditableprofile.model.ProfileItem
import com.farhad.sparkeditableprofile.updateProfile.view.FragUpdateProfile
import com.farhad.sparkeditableprofile.viewProfile.view.FragViewProfile
import dagger.BindsInstance
import dagger.Component

@Component(modules = [ViewModelModule::class, DataModule::class, DomainModule::class])
interface ViewModelComponent {
    @Component.Builder
    interface Builder {
        fun build(): ViewModelComponent

        @BindsInstance
        fun profileItem(profileItem: ProfileItem?): Builder

    }
    fun injectFragment(fragUpdateProfile: FragUpdateProfile)
    fun injectFragment(fragViewProfile: FragViewProfile)
}