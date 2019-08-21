package com.farhad.sparkeditableprofile.utils

import android.graphics.Bitmap
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random
import kotlin.random.nextInt

@RunWith(AndroidJUnit4::class)
class CropImageTest{
    @Test
    fun test(){
        val randomLength1 = Random.nextInt(0..10000)
        val randomLength2 = Random.nextInt(0..10000)
        val croppedWith = Random.nextInt(0..10000)
        val croppedHeight = Random.nextInt(0..10000)

        val bitmap1 = Bitmap.createBitmap(randomLength1, randomLength2, Bitmap.Config.ARGB_8888)
        val cropped1 = CropImage().crop(bitmap1, croppedWith, croppedHeight)

        val bitmap2 = Bitmap.createBitmap(randomLength2, randomLength1, Bitmap.Config.ARGB_8888)
        val cropped2 = CropImage().crop(bitmap2, croppedWith, croppedHeight)

        val bitmap3 = Bitmap.createBitmap(randomLength1, randomLength1, Bitmap.Config.ARGB_8888)
        val cropped3 = CropImage().crop(bitmap3, croppedWith, croppedHeight)

        assertEquals(croppedWith, cropped1.width)
        assertEquals(croppedHeight, cropped1.height)

        assertEquals(croppedWith, cropped2.width)
        assertEquals(croppedHeight, cropped2.height)

        assertEquals(croppedWith, cropped3.width)
        assertEquals(croppedHeight, cropped3.height)
    }
}