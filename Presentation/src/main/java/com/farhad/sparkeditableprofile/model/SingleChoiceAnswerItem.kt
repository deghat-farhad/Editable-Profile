package com.farhad.sparkeditableprofile.model

import android.os.Parcel
import android.os.Parcelable

data class SingleChoiceAnswerItem(
    val id: String? = null,
    val name: String? = null
) : Parcelable {
    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<SingleChoiceAnswerItem> {
            override fun createFromParcel(parcel: Parcel) =
                SingleChoiceAnswerItem(parcel)

            override fun newArray(size: Int) = arrayOfNulls<SingleChoiceAnswerItem>(size)
        }
    }

    private constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(name)
    }

    override fun describeContents() = 0
}