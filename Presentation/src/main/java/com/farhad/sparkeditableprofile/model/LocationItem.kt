package com.farhad.sparkeditableprofile.model

import android.os.Parcel
import android.os.Parcelable

data class LocationItem (
    val lat: String? = null,
    val lon: String? = null,
    val city: String?  = null
) : Parcelable {
    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<LocationItem> {
            override fun createFromParcel(parcel: Parcel) = LocationItem(parcel)
            override fun newArray(size: Int) = arrayOfNulls<LocationItem>(size)
        }
    }

    private constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(lat)
        dest.writeString(lon)
        dest.writeString(city)
    }

    override fun describeContents() = 0
}
