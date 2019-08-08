package com.farhad.sparkeditableprofile.updateProfile.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.farhad.sparkeditableprofile.R
import com.farhad.sparkeditableprofile.di.DaggerViewModelComponent
import com.farhad.sparkeditableprofile.di.ViewModelFactory
import com.farhad.sparkeditableprofile.updateProfile.model.SingleChoiceAnswerItem
import com.farhad.sparkeditableprofile.updateProfile.viewModel.UpdateProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import javax.inject.Inject

class FragUpdateProfile: Fragment(){

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: UpdateProfileViewModel

    private lateinit var singleChoiceItemsContainer: LinearLayout
    var singleChoiceTextInputs = HashMap<String, TextInputEditText>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(
            R.layout.frag_update_profile,
            container, false)

        singleChoiceItemsContainer = view.findViewById(R.id.singleChoiceItemsContainer)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        injectThisToDagger()
        initViews(view)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UpdateProfileViewModel::class.java)
        setObservers()
    }

    private fun injectThisToDagger(){
        DaggerViewModelComponent
            .builder()
            .build()
            .injectFragment(this)
    }

    private fun initViews(fragContainer: View) {
        singleChoiceItemsContainer = fragContainer.findViewById(R.id.singleChoiceItemsContainer)
    }

    private fun setObservers() {

    }

    fun addSingleChoiceQuestions(questionsMap: HashMap<String, List<SingleChoiceAnswerItem>>){
        context?.let { context ->
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            for(questionsKey in questionsMap.keys){
                val view = layoutInflater.inflate(R.layout.single_choice_layout, null)
                val txtInputLayout = view.findViewById<TextInputLayout>(R.id.txtInputLayoutSingleSelection)
                val txtViewInput = view.findViewById<TextInputEditText>(R.id.txtInputEdtTxtSingleSelection)

                txtInputLayout.hint = questionsKey
                singleChoiceTextInputs[questionsKey] = txtViewInput

                singleChoiceItemsContainer.addView(view)

                txtViewInput.setOnClickListener {
                    questionsMap[questionsKey]?.let{ answers ->
                        val answersStringList = answers.map { it.name }
                        val index = if(txtViewInput.text == null)
                            -1
                        else
                            answersStringList.indexOf(txtViewInput.text.toString())

                        displayDialog(answersStringList, questionsKey, index)
                    }
                }
            }
        }
    }

    private fun displayDialog(answers: List<String?>, title: String, selectedItem: Int) {
        val dialogBuilder = MaterialAlertDialogBuilder(
            context,
            R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered
        )

        var innerSelectedItem = selectedItem

        dialogBuilder.setSingleChoiceItems(answers.toTypedArray(), selectedItem) { dialog, which ->
            innerSelectedItem = which
        }
            .setTitle(title)
            .setPositiveButton(getString(R.string.btnOK)) { dialog, which ->
                singleChoiceTextInputs[title]?.setText(answers[innerSelectedItem])
            }
            .setNegativeButton(getString(R.string.btnCancel)) { dialog, which -> }
            .create().show()
    }
}

