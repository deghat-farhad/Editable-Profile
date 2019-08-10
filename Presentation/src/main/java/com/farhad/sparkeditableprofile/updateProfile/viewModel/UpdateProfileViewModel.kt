package com.farhad.sparkeditableprofile.updateProfile.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.domain.model.RequestStatus
import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer
import com.farhad.sparkeditableprofile.domain.usecase.base.DefaultObserver
import com.farhad.sparkeditableprofile.domain.usecase.getLocations.GetLocations
import com.farhad.sparkeditableprofile.domain.usecase.getSingleChoiceAnswers.GetSingleChoiceAnswers
import com.farhad.sparkeditableprofile.domain.usecase.registerProfile.RegisterProfile
import com.farhad.sparkeditableprofile.domain.usecase.registerProfile.RegisterProfileParams
import com.farhad.sparkeditableprofile.mapper.LocationItemMapper
import com.farhad.sparkeditableprofile.mapper.ProfileItemMapper
import com.farhad.sparkeditableprofile.mapper.SingleChoiceAnswerItemMapper
import com.farhad.sparkeditableprofile.model.LocationItem
import com.farhad.sparkeditableprofile.model.ProfileItem
import com.farhad.sparkeditableprofile.model.SingleChoiceAnswerItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

open class UpdateProfileViewModel @Inject constructor(
    private val singleChoiceAnswerItemMapper: SingleChoiceAnswerItemMapper,
    private val locationItemMapper: LocationItemMapper,
    private val getSingleChoiceAnswers: GetSingleChoiceAnswers,
    private val getLocations: GetLocations,
    private val registerProfile: RegisterProfile,
    private val profileItemMapper: ProfileItemMapper
): ViewModel() {
    private val bag = CompositeDisposable()
    lateinit var questionLocations: List<LocationItem>
    var newBirthDay: Date? = null


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

    open fun submit(
        displayName: String,
        realName: String,
        occupation: String,
        aboutMe: String,
        city: String,
        height: Int,
        answers: HashMap<String, String>
    ) {
        val profileItem = ProfileItem(randomId())

        if (displayName.isNotEmpty()) profileItem.displayName = displayName
        if (realName.isNotEmpty()) profileItem.realName = realName
        if (occupation.isNotEmpty()) profileItem.occupation = occupation
        if (aboutMe.isNotEmpty()) profileItem.aboutMe = aboutMe
        profileItem.height = height
        val newLocationItem = questionLocationsStrings.value?.indexOf(city)
        newLocationItem?.let {
            if (it > 0) {
                profileItem.location = questionLocations[it]
            }
        }
        val newAnswers = HashMap<String, SingleChoiceAnswerItem>()
        for (question in answers.keys) {
            questionSingleChoices.value?.let { questionSingleChoicesListMap ->
                val singleChoiceAnswerItemList = questionSingleChoicesListMap[question]
                singleChoiceAnswerItemList?.let { singleChoiceAnswerItems ->
                    val index = singleChoiceAnswerItems.map { it.name }.indexOf(answers[question])
                    if (index > 0)
                        newAnswers[question] = singleChoiceAnswerItems[index]
                }
            }
        }
        profileItem.answers = newAnswers
        profileItem.birthday = newBirthDay

        registerProfile(profileItem)
    }

    private fun registerProfile(profileItem: ProfileItem) {

        val updateProfileObserver = object : DefaultObserver<RequestStatus>() {
            override fun onNext(it: RequestStatus) {
                super.onNext(it)
                println(profileItem.id)
            }
        }

        val params = RegisterProfileParams(profileItemMapper.mapToDomain(profileItem))
        registerProfile.execute(updateProfileObserver, params)?.addTo(bag)
    }

    private fun randomId(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val length = 64
        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}