package com.farhad.sparkeditableprofile.viewProfile.viewModel

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farhad.editableprofile.utils.SingleLiveEvent
import com.farhad.sparkeditableprofile.domain.model.Profile
import com.farhad.sparkeditableprofile.domain.usecase.base.DefaultObserver
import com.farhad.sparkeditableprofile.domain.usecase.getProfile.GetProfile
import com.farhad.sparkeditableprofile.domain.usecase.getProfile.GetProfileParams
import com.farhad.sparkeditableprofile.mapper.ProfileItemMapper
import com.farhad.sparkeditableprofile.model.LocationItem
import com.farhad.sparkeditableprofile.model.ProfileItem
import com.farhad.sparkeditableprofile.model.SingleChoiceAnswerItem
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

open class ViewProfileViewModel @Inject constructor(
    private val getProfile: GetProfile,
    private val profileItemMapper: ProfileItemMapper
) : ViewModel() {

    open val displayName: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    open val profilePicture: MutableLiveData<Bitmap> by lazy { MutableLiveData<Bitmap>() }
    open val birthday: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    open val height: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    open val aboutMe: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    open val location: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    open val answers: MutableLiveData<HashMap<String, String>> by lazy { MutableLiveData<HashMap<String, String>>() }
    open val navigateToEditProfile: SingleLiveEvent<ProfileItem> by lazy { SingleLiveEvent<ProfileItem>() }

    private lateinit var profileItem: ProfileItem
    private var formatter = SimpleDateFormat("d MMM yyyy", Locale.US)

    open fun getProfile(id: String) {
        //val id = "9VmW3YVQ6aw73lSTYOFtjlMLtNHBTj77X13PI6Lyo3Nr3RvxDWy167hluhNi9NG8"

        val observable = object : DefaultObserver<Profile>() {
            override fun onNext(it: Profile) {
                super.onNext(it)
                val profileItem: ProfileItem = profileItemMapper.mapToPresentation(it)

                this@ViewProfileViewModel.profileItem = profileItem

                displayName.value = profileItem.displayName
                profileItem.birthday?.let {
                    birthday.value = formatter.format(profileItem.birthday)
                }
                height.value = profileItem.height.toString()
                aboutMe.value = profileItem.aboutMe
                setLiveLocation(profileItem.location)
                setLiveAnswers(profileItem.answers)
                setLiveProfilePicture(profileItem.profilePicture?.url)
            }
        }

        val getProfileParams = GetProfileParams(id)

        getProfile.execute(observable, getProfileParams)
    }

    private fun setLiveLocation(locationItem: LocationItem?) {
        locationItem?.let { notNullLocationItem -> this.location.value = notNullLocationItem.city }
    }

    private fun setLiveAnswers(answers: HashMap<String, SingleChoiceAnswerItem>) {
        val answersTmp: HashMap<String, String> = hashMapOf()
        for (profileAnswerKey in answers.keys) {
            answers[profileAnswerKey]?.name?.let { profileAnswerName ->
                answersTmp[profileAnswerKey] = profileAnswerName
            }
        }
        this.answers.value = answersTmp
    }

    private fun setLiveProfilePicture(profilePictureUrl: String?) {
        profilePictureUrl?.let { notNullProfilePictureUrl ->
            val target = object : com.squareup.picasso.Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    bitmap?.let { profilePicture.value = it }
                }
            }
            Picasso.get().load(notNullProfilePictureUrl).into(target)
        }
    }

    fun updateProfile(){
        navigateToEditProfile.value = profileItem
    }
}