package com.farhad.sparkeditableprofile.data.di

import com.farhad.sparkeditableprofile.data.repository.ProfileRepositoryImpl
import org.junit.Test
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert.assertThat

class DependencyInjectionTest {

    @Test
    fun getProfileRepositoryImpl(){
        val profileRepositoryImpl = DaggerDataComponent.create().getProfileRepositoryImpl()
        assertThat(profileRepositoryImpl, instanceOf(ProfileRepositoryImpl::class.java))
    }
}