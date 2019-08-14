package com.farhad.sparkeditableprofile.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*


class ProfileItem(
    var id: String,
    var displayName: String? = null,
    var realName: String? = null,
    var profilePicture: ProfilePictureItem? = null,
    var birthday: Date? = null,
    var height: Int? = null,
    var occupation: String? = null,
    var aboutMe: String? = null,
    var location: LocationItem? = null,
    var answers: HashMap<String, SingleChoiceAnswerItem> = hashMapOf()

) : Parcelable {
    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<ProfileItem> {
            override fun newArray(size: Int) = arrayOfNulls<ProfileItem>(size)
            override fun createFromParcel(source: Parcel) = ProfileItem(source)
        }
    }

    private constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(ProfilePictureItem::class.java.classLoader),
        parcel.readSerializable() as Date,
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(LocationItem::class.java.classLoader),
        parcel.readHashMap(SingleChoiceAnswerItem::class.java.classLoader) as HashMap<String, SingleChoiceAnswerItem>
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(displayName)
        dest.writeString(realName)
        dest.writeParcelable(profilePicture, flags)
        dest.writeSerializable(birthday)
        height?.let { dest.writeInt(it) }
        dest.writeString(occupation)
        dest.writeString(aboutMe)
        dest.writeParcelable(location, flags)
        dest.writeMap(answers)
    }

    override fun describeContents() = 0

}