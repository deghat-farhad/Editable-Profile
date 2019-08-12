package com.farhad.sparkeditableprofile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.farhad.sparkeditableprofile.viewProfile.view.FragViewProfile

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
