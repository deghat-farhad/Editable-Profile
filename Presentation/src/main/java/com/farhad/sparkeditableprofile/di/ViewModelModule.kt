package com.farhad.sparkeditableprofile.di

import androidx.lifecycle.ViewModel
import com.farhad.sparkeditableprofile.updateProfile.viewModel.UpdateProfileViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(UpdateProfileViewModel::class)
    fun bindProfileEditViewModel(profileEditViewModel: UpdateProfileViewModel): ViewModel
}