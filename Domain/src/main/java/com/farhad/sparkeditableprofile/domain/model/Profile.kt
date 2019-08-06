package com.farhad.sparkeditableprofile.domain.model

import java.util.*
import kotlin.collections.HashMap
data class Profile (
    val id: String,
    val displayName: String? = null,
    val realName: String? = null,
    val profilePicture: ProfilePicture? = null,
    val birthday: Date? = null,
    val height: Int? = null,
    val occupation: String? = null,
    val aboutMe: String? = null,
    val location: Location? = null,
    val answers: HashMap<String, SingleChoiceAnswer>
)