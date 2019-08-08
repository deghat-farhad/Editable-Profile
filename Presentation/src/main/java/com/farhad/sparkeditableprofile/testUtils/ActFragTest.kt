package com.farhad.sparkeditableprofile.testUtils

import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RestrictTo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

@RestrictTo(RestrictTo.Scope.TESTS)
class ActFragTest : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val content = FrameLayout(this)
        content.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        content.id = com.farhad.sparkeditableprofile.R.id.frameLayoutMain
        setContentView(content)
    }

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(com.farhad.sparkeditableprofile.R.id.frameLayoutMain, fragment, "TEST")
            .commit()
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(com.farhad.sparkeditableprofile.R.id.frameLayoutMain, fragment).commit()
    }
}
