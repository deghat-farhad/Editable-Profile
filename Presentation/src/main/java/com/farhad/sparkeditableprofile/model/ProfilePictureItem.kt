package com.farhad.sparkeditableprofile.model

import android.os.Parcel
import android.os.Parcelable

data class ProfilePictureItem(
    val url: String?
) : Parcelable {
    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<ProfilePictureItem> {
            override fun createFromParcel(parcel: Parcel) =
                ProfilePictureItem(parcel)

            override fun newArray(size: Int) = arrayOfNulls<ProfilePictureItem>(size)
        }
    }

    private constructor(parcel: Parcel) : this(
        parcel.readString()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(url)
    }

    override fun describeContents() = 0
}