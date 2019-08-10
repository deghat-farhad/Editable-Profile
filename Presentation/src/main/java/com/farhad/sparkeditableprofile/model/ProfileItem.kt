package com.farhad.sparkeditableprofile.model

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

)