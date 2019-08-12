package com.farhad.sparkeditableprofile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.farhad.sparkeditableprofile.data.di.DaggerDataComponent
import com.farhad.sparkeditableprofile.data.repository.ProfileRepositoryImpl
import com.farhad.sparkeditableprofile.domain.model.Profile
import com.farhad.sparkeditableprofile.domain.usecase.base.DefaultObserver
import com.farhad.sparkeditableprofile.domain.usecase.getProfile.GetProfile
import com.farhad.sparkeditableprofile.domain.usecase.getProfile.GetProfileParams
import com.farhad.sparkeditableprofile.updateProfile.view.FragUpdateProfile
import com.farhad.sparkeditableprofile.viewProfile.view.FragViewProfile
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(
                R.id.frameLayoutMain,
                FragViewProfile()
            ).commit()
        }
    }
}
