package com.farhad.sparkeditableprofile.di


import com.farhad.sparkeditableprofile.model.ProfileItem
import com.farhad.sparkeditableprofile.updateProfile.view.FragUpdateProfile
import com.farhad.sparkeditableprofile.viewProfile.view.FragViewProfile
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named

@Component(modules = [ViewModelModule::class, DataModule::class, DomainModule::class])
interface ViewModelComponent {
    @Component.Builder
    interface Builder {
        fun build(): ViewModelComponent

        @BindsInstance
        fun profileItem(profileItem: ProfileItem?): Builder

        @BindsInstance
        fun notAValidCity(@Named("notAValidCity") emptyLocationMessage: String = ""): Builder
        @BindsInstance
        fun emptyFieldMessage(@Named("notEmptyMessage") emptyLocationMessage: String = ""): Builder
        @BindsInstance
        fun youngerThanMessage(@Named("youngerMessage") emptyLocationMessage: String = ""): Builder
        @BindsInstance
        fun tooLongMessage(@Named("tooLongMessage") emptyLocationMessage: String = ""): Builder
        @BindsInstance
        fun charactersNotAllowedMessage(@Named("charactersNotAllowedMessage") emptyLocationMessage: String = ""): Builder


    }
    fun injectFragment(fragUpdateProfile: FragUpdateProfile)
    fun injectFragment(fragViewProfile: FragViewProfile)
}