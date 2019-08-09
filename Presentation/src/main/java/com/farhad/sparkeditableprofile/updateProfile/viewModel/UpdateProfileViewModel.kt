package com.farhad.sparkeditableprofile.updateProfile.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer
import com.farhad.sparkeditableprofile.domain.usecase.base.DefaultObserver
import com.farhad.sparkeditableprofile.domain.usecase.getLocations.GetLocations
import com.farhad.sparkeditableprofile.domain.usecase.getSingleChoiceAnswers.GetSingleChoiceAnswers
import com.farhad.sparkeditableprofile.mapper.LocationItemMapper
import com.farhad.sparkeditableprofile.mapper.SingleChoiceAnswerItemMapper
import com.farhad.sparkeditableprofile.model.LocationItem
import com.farhad.sparkeditableprofile.model.SingleChoiceAnswerItem
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

open class UpdateProfileViewModel @Inject constructor(
    private val singleChoiceAnswerItemMapper: SingleChoiceAnswerItemMapper,
    private val locationItemMapper: LocationItemMapper,
    private val getSingleChoiceAnswers: GetSingleChoiceAnswers,
    private val getLocations: GetLocations
): ViewModel() {
    private lateinit var questionLocations: List<LocationItem>
    private var newBirthDay: Date? = null


    open val questionSingleChoices = MutableLiveData<HashMap<String, List<SingleChoiceAnswerItem>>>()
    open val questionLocationsStrings: MutableLiveData<List<String?>> by lazy { MutableLiveData<List<String?>>() }
    open val birthday: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    open val profilePicture: MutableLiveData<Bitmap> by lazy { MutableLiveData<Bitmap>() }




    init {
        getSingleChoiceQuestions()
        getLocations()
    }

    private fun getSingleChoiceQuestions() {
        val getSingleChoiceQuestionObserver = object : DefaultObserver<HashMap<String, List<SingleChoiceAnswer>>>() {
            override fun onNext(it: HashMap<String, List<SingleChoiceAnswer>>) {
                super.onNext(it)
                questionSingleChoices.value = singleChoiceAnswerItemMapper.mapListHashMapToPresentation(it)
            }
        }
        getSingleChoiceAnswers.execute(getSingleChoiceQuestionObserver, Unit)
    }

    private fun getLocations(){
        val getLocationsObserver = object : DefaultObserver<List<Location>>(){
            override fun onNext(it: List<Location>) {
                super.onNext(it)
                questionLocations = locationItemMapper.mapToPresentation(it)
                val cities = questionLocations.map { it.city }
                questionLocationsStrings.value = cities
            }
        }
        getLocations.execute(getLocationsObserver, Unit)
    }

    open fun setNewBirthday(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, monthOfYear, dayOfMonth)
        newBirthDay = calendar.time
        birthday.value = formatDate(calendar.time)
    }

    private fun formatDate(date: Date?): String {
        val formatter = SimpleDateFormat("d MMM yyyy", Locale.US)
        return formatter.format(date)
    }

    open fun setProfilePicture(profilePicture: Bitmap) {
        this.profilePicture.value = profilePicture
    }
}