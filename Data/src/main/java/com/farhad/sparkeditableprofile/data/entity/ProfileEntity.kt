package com.farhad.sparkeditableprofile.data.entity

import java.util.*
import kotlin.collections.HashMap
data class ProfileEntity (
    var id: String,
    var Display_Name: String? = null,
    var Real_Name: String? = null,
    var Profile_Picture: String? = null,
    var Birthday: Date? = null,
    var Height: Int? = null,
    var Occupation: String? = null,
    var About_Me: String? = null,
    var location: LocationEntity? = null,
    var answers: Map<String, SingleChoiceAnswerEntity> = mapOf()
)