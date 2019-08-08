package com.farhad.sparkeditableprofile.updateProfile.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.farhad.sparkeditableprofile.domain.model.SingleChoiceAnswer
import com.farhad.sparkeditableprofile.domain.repository.QuestionRepository
import com.farhad.sparkeditableprofile.domain.usecase.base.DefaultObserver
import com.farhad.sparkeditableprofile.domain.usecase.getSingleChoiceAnswers.GetSingleChoiceAnswers
import com.farhad.sparkeditableprofile.mapper.SingleChoiceAnswerItemMapper
import com.farhad.sparkeditableprofile.model.SingleChoiceAnswerItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

open class UpdateProfileViewModel @Inject constructor(
    private val singleChoiceAnswerItemMapper: SingleChoiceAnswerItemMapper,
    private val getSingleChoiceAnswers: GetSingleChoiceAnswers
): ViewModel() {

    open val questionSingleChoices = MutableLiveData<HashMap<String, List<SingleChoiceAnswerItem>>>()


    init {
        getSingleChoiceQuestions()
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

}