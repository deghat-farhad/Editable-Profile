package com.farhad.sparkeditableprofile.updateProfile.viewModel

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farhad.editableprofile.utils.SingleLiveEvent
import com.farhad.sparkeditableprofile.domain.model.Location
import com.farhad.sparkeditableprofile.domain.model.ProfilePicture
import com.farhad.sparkeditableprofile.domain.model.RequestStatus
import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer
import com.farhad.sparkeditableprofile.domain.usecase.base.DefaultObserver
import com.farhad.sparkeditableprofile.domain.usecase.getLocations.GetLocations
import com.farhad.sparkeditableprofile.domain.usecase.getSingleChoiceAnswers.GetSingleChoiceAnswers
import com.farhad.sparkeditableprofile.domain.usecase.registerProfile.RegisterProfile
import com.farhad.sparkeditableprofile.domain.usecase.registerProfile.RegisterProfileParams
import com.farhad.sparkeditableprofile.domain.usecase.updateProfile.UpdateProfile
import com.farhad.sparkeditableprofile.domain.usecase.updateProfile.UpdateProfileParams
import com.farhad.sparkeditableprofile.domain.usecase.uploadProfilePicture.UploadProfilePicture
import com.farhad.sparkeditableprofile.domain.usecase.uploadProfilePicture.UploadProfilePictureParams
import com.farhad.sparkeditableprofile.mapper.LocationItemMapper
import com.farhad.sparkeditableprofile.mapper.ProfileItemMapper
import com.farhad.sparkeditableprofile.mapper.SingleChoiceAnswerItemMapper
import com.farhad.sparkeditableprofile.model.LocationItem
import com.farhad.sparkeditableprofile.model.ProfileItem
import com.farhad.sparkeditableprofile.model.SingleChoiceAnswerItem
import com.farhad.sparkeditableprofile.updateProfile.viewModel.validator.DateValidator
import com.farhad.sparkeditableprofile.updateProfile.viewModel.validator.LocationValidator
import com.farhad.sparkeditableprofile.updateProfile.viewModel.validator.TextValidator
import com.farhad.sparkeditableprofile.updateProfile.viewModel.validator.ValidationException
import com.farhad.sparkeditableprofile.utils.CropImage
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

const val MINIMUM_AGE = 18
open class UpdateProfileViewModel @Inject constructor(
    private val singleChoiceAnswerItemMapper: SingleChoiceAnswerItemMapper,
    private val locationItemMapper: LocationItemMapper,
    private val getSingleChoiceAnswers: GetSingleChoiceAnswers,
    private val getLocations: GetLocations,
    private val registerProfile: RegisterProfile,
    private val profileItemMapper: ProfileItemMapper,
    private val uploadProfilePicture: UploadProfilePicture,
    var profileItem: ProfileItem?,
    private val updateProfile: UpdateProfile,
    private val profileItemDiff: ProfileItemDiff,
    private val cropImage: CropImage,
    private val textValidator: TextValidator,
    private val dateValidator: DateValidator,
    private val locationValidator: LocationValidator
): ViewModel() {
    private val bag = CompositeDisposable()
    lateinit var questionLocations: List<LocationItem>
    var newBirthDay: Date? = null
    var profilePictureFile: File? = null
    private var itsAnUpdate = false



    open val questionSingleChoices = MutableLiveData<HashMap<String, List<SingleChoiceAnswerItem>>>()
    open val questionLocationsStrings: MutableLiveData<List<String?>> by lazy { MutableLiveData<List<String?>>() }
    open val birthday: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    open val profilePicture: MutableLiveData<Bitmap> by lazy { MutableLiveData<Bitmap>() }
    open val profileRegistered: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }
    open val profileUpdated: SingleLiveEvent<Unit> by lazy { SingleLiveEvent<Unit>() }
    open val registerIsInProgress: SingleLiveEvent<Unit> by lazy { SingleLiveEvent<Unit>() }

    open val displayName: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    open val realName: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    open val occupation: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    open val height: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    open val aboutMe: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    open val location: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    open val answers: MutableLiveData<HashMap<String, String>> by lazy { MutableLiveData<HashMap<String, String>>() }

    open val birthDayValidation: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }
    open val displayNameValidation: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }
    open val realNameValidation: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }
    open val aboutMeValidation: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }
    open val occupationValidation: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }
    open val heightValidation: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }
    open val locationValidation: SingleLiveEvent<String> by lazy { SingleLiveEvent<String>() }


    init {
        profileItem?.let { profileItem ->
            itsAnUpdate = true
            displayName.value = profileItem.displayName
            realName.value = profileItem.realName
            occupation.value = profileItem.occupation
            profileItem.birthday?.let {
                birthday.value = formatDate(profileItem.birthday)
            }
            height.value = profileItem.height.toString()
            aboutMe.value = profileItem.aboutMe
            setLiveLocation(profileItem.location)
            setLiveProfilePicture(profileItem.profilePicture?.url)
        }
        getSingleChoiceQuestions()
        getLocations()
    }

    private fun getSingleChoiceQuestions() {
        val getSingleChoiceQuestionObserver = object : DefaultObserver<HashMap<String, List<SingleChoiceAnswer>>>() {
            override fun onNext(it: HashMap<String, List<SingleChoiceAnswer>>) {
                super.onNext(it)
                questionSingleChoices.value = singleChoiceAnswerItemMapper.mapListHashMapToPresentation(it)
                profileItem?.let { setLiveAnswers(it.answers) }
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

    open fun setProfilePicture(profilePictureBitmap: Bitmap?, cacheDir: String) {
        profilePictureBitmap?.let{
            this.profilePicture.value = saveBitmap(it, cacheDir)
        }
    }

    private fun saveBitmap(profilePictureBitmap: Bitmap, cacheDir: String): Bitmap{

        val output = cropImage.crop(profilePictureBitmap, 256, 256)
        profilePictureFile = File(cacheDir, "tmp.jpg")
        profilePictureFile?.let {
            it.createNewFile()

            val bos = ByteArrayOutputStream()
            output.compress(Bitmap.CompressFormat.JPEG, 90, bos)
            val bitmapData = bos.toByteArray()

            val fos = FileOutputStream(profilePictureFile)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        }
        return output
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

        if (validateProfileItem(profileItem)) {

            //there is no profile so we`re going to register one.
            if (this.profileItem == null)
                registerProfile(profileItem)

            //a profile exist so we try to update current profile if tehre is any changes
            this.profileItem?.let {
                if (profileItem != it) {
                    updateProfile(it, profileItem)
                }
                profilePictureFile?.let { file ->
                    uploadPicture(it, file)
                }
            }
        }
    }

    private fun registerProfile(profileItem: ProfileItem) {

        registerIsInProgress.call()
        val updateProfileObserver = object : DefaultObserver<RequestStatus>() {
            override fun onNext(it: RequestStatus) {
                super.onNext(it)
                if(profilePictureFile == null)
                    profileRegistered.value = profileItem.id
                else
                profilePictureFile?.let {
                    uploadPicture(profileItem, it)
                }
            }
        }

        val params = RegisterProfileParams(profileItemMapper.mapToDomain(profileItem))
        registerProfile.execute(updateProfileObserver, params)?.addTo(bag)
    }

    private fun updateProfile(oldProfile: ProfileItem, newProfileItem: ProfileItem) {
        val diffProfile = profileItemDiff.getDiff(oldProfile, newProfileItem)

        val updateProfileObserver = object : DefaultObserver<RequestStatus>() {
            override fun onComplete() {
                super.onComplete()
                if(profilePictureFile == null)
                    this@UpdateProfileViewModel.profileUpdated.call()
            }
        }

        val params = UpdateProfileParams(profileItemMapper.mapToDomain(diffProfile))
        updateProfile.execute(updateProfileObserver, params)
    }

    private fun validateProfileItem(profileItem: ProfileItem): Boolean {
        var isValidate = true
        try {
            textValidator.validate(profileItem.displayName ?: "", 256, false, Regex("^|[A-Za-z0-9._\\-\\s]+"))
            displayNameValidation.value = ""
        } catch (e: ValidationException) {
            displayNameValidation.value = e.message
            isValidate = false
        }

        try {
            textValidator.validate(profileItem.realName ?: "", 256, false, Regex("^|[A-Za-z0-9._\\-\\s]+"))
            realNameValidation.value = ""
        } catch (e: ValidationException) {
            realNameValidation.value = e.message
            isValidate = false
        }

        try {
            textValidator.validate(profileItem.aboutMe ?: "", 5000, true, Regex("^|[A-Za-z0-9!?()@#\$&'.,_\\-\\s]+"))
            aboutMeValidation.value = ""
        } catch (e: ValidationException) {
            aboutMeValidation.value = e.message
            isValidate = false
        }

        try {
            textValidator.validate(profileItem.occupation ?: "", 256, true, Regex("^|[A-Za-z0-9!?()@#\$&'.,_\\-\\s]+"))
            occupationValidation.value = ""
        } catch (e: ValidationException) {
            occupationValidation.value = e.message
            isValidate = false
        }

        try {
            val heightString = if (profileItem.height != -1) profileItem.height.toString() else ""
            textValidator.validate(heightString, 3, false, Regex("^|[0-9]+"))
            heightValidation.value = ""
        } catch (e: ValidationException) {
            heightValidation.value = e.message
            isValidate = false
        }

        try {
            dateValidator.confirmIsOlderThan(profileItem.birthday, MINIMUM_AGE)
            birthDayValidation.value = ""
        } catch (e: ValidationException) {
            birthDayValidation.value = e.message
            isValidate = false
        }

        try {
            locationValidator.validate(profileItem.location)
            locationValidation.value = ""
        } catch (e: ValidationException) {
            locationValidation.value = e.message
            isValidate = false
        }
        return isValidate
    }

    private fun randomId(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val length = 64
        return (1..length)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    open fun uploadPicture(profileItem: ProfileItem, profilePicture: File){
        val uploadProfilePictureObserver = object: DefaultObserver<ProfilePicture>(){
            override fun onNext(it: ProfilePicture) {
                super.onNext(it)
                if(itsAnUpdate)
                    this@UpdateProfileViewModel.profileUpdated.call()
                else
                    profileRegistered.value = (profileItem.id)
            }
        }
        val params = UploadProfilePictureParams(profilePicture, profileItemMapper.mapToDomain(profileItem))
        uploadProfilePicture.execute(uploadProfilePictureObserver, params)
    }

    private fun setLiveLocation(locationItem: LocationItem?) {
        locationItem?.let { notNullLocationItem -> this.location.value = notNullLocationItem.city }
    }

    open fun setLiveAnswers(answers: HashMap<String, SingleChoiceAnswerItem>) {
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
            Picasso.get().load(notNullProfilePictureUrl)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(target)
        }
    }
}