package com.farhad.sparkeditableprofile.updateProfile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.farhad.sparkeditableprofile.R

class FragUpdateProfile: Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(
            R.layout.frag_update_profile,
            container, false)
    }
}

