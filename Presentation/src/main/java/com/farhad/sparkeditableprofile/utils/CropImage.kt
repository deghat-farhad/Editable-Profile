package com.farhad.sparkeditableprofile.utils

import android.graphics.Bitmap
import javax.inject.Inject

class CropImage @Inject constructor(){

    fun crop(bitmap: Bitmap, with: Int, height: Int): Bitmap {
        val croppedBitmap = if (bitmap.width >= bitmap.height){

            Bitmap.createBitmap(
                bitmap,
                bitmap.width /2 - bitmap.height /2,
                0,
                bitmap.height,
                bitmap.height
            )

        }else{

            Bitmap.createBitmap(
                bitmap,
                0,
                bitmap.height /2 - bitmap.width /2,
                bitmap.width,
                bitmap.width
            )
        }

        return Bitmap.createScaledBitmap(croppedBitmap, with, height, false)
    }
}