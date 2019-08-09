package com.farhad.sparkeditableprofile.updateProfile.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RestrictTo
import androidx.core.content.ContextCompat
import org.hamcrest.TypeSafeMatcher

@RestrictTo(RestrictTo.Scope.TESTS)
class DrawableMatcher(@DrawableRes expectedResourceId: Int) : TypeSafeMatcher<View>() {
    private var  expectedResourceId = 0
    init{
        this.expectedResourceId = expectedResourceId
    }
    override fun describeTo(description: org.hamcrest.Description) {
        description.appendText("with drawable id: ").appendValue(expectedResourceId)
    }

    override fun matchesSafely(item: View?): Boolean {
        if (item !is ImageView) return false

        val imageView: ImageView = item
        if (expectedResourceId < 0) return imageView.drawable == null

        val expectedDrawable = ContextCompat.getDrawable(item.getContext(), expectedResourceId) ?: return false

        val actualDrawable = imageView.drawable

        if (expectedDrawable is VectorDrawable) {
            if (actualDrawable !is VectorDrawable) return false
            return vectorToBitmap(expectedDrawable).sameAs(vectorToBitmap(actualDrawable))
        }

        if (expectedDrawable is BitmapDrawable) {
            if (actualDrawable !is BitmapDrawable) return false
            return (expectedDrawable).bitmap.sameAs(actualDrawable.bitmap)
        }

        throw IllegalArgumentException("Unsupported drawable: " + imageView.drawable)
    }

    private fun vectorToBitmap(vectorDrawable: VectorDrawable): Bitmap {
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }
}