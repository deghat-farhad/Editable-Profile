package com.farhad.sparkeditableprofile

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment


const val PREFS_NAME = "Settings"
const val PROFILE_ID_KEY = "ProfileId"
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setNavigation()
        setToolbar()
    }

    private fun setNavigation() {
        val settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val profileId: String = settings.getString(PROFILE_ID_KEY, "") ?: ""

        val navHost = supportFragmentManager.findFragmentById(R.id.navHostFragment)
                as NavHostFragment? ?: return
        val navController = navHost.navController


        val inflater = navController.navInflater
        val graph = inflater.inflate(R.navigation.navigation)

        graph.startDestination = if (profileId.isEmpty())
            R.id.fragUpdateProfile
        else
            R.id.fragViewProfile

        navController.graph = graph
    }

    private fun setToolbar(){
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }
}
