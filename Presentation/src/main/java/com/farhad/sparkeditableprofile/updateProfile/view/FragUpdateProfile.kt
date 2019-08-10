package com.farhad.sparkeditableprofile.updateProfile.view

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.farhad.sparkeditableprofile.R
import com.farhad.sparkeditableprofile.di.DaggerViewModelComponent
import com.farhad.sparkeditableprofile.di.ViewModelFactory
import com.farhad.sparkeditableprofile.model.SingleChoiceAnswerItem
import com.farhad.sparkeditableprofile.updateProfile.viewModel.UpdateProfileViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap
import kotlin.math.min

const val PICK_IMAGE = 1
class FragUpdateProfile: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: UpdateProfileViewModel

    private lateinit var singleChoiceItemsContainer: LinearLayout
    private var singleChoiceTextInputs = HashMap<String, TextInputEditText>()
    private lateinit var autoCompleteTxtViewLocation: AutoCompleteTextView
    private lateinit var txtInputEdtTxtBirthday: TextInputEditText
    private lateinit var imgViewEditProfilePic: ImageView
    private lateinit var btnAddProfilePic: ImageButton
    private lateinit var btnSubmit: Button
    private lateinit var txtInputEdtTxtDisplayName: TextInputEditText
    private lateinit var txtInputEdtTxtAboutMe: TextInputEditText
    private lateinit var txtInputEdtTxtRealName: TextInputEditText
    private lateinit var txtInputEdtTxtOccupation: TextInputEditText
    private lateinit var txtInputEdtTxtHeight: TextInputEditText


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(
            R.layout.frag_update_profile,
            container, false
        )

        singleChoiceItemsContainer = view.findViewById(R.id.singleChoiceItemsContainer)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        if (!::viewModelFactory.isInitialized) {
            injectThisToDagger()
        }
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UpdateProfileViewModel::class.java)
        setObservers()
    }

    private fun injectThisToDagger() {
        DaggerViewModelComponent
            .builder()
            .build()
            .injectFragment(this)
    }

    private fun initViews(fragContainer: View) {
        singleChoiceItemsContainer = fragContainer.findViewById(R.id.singleChoiceItemsContainer)
        autoCompleteTxtViewLocation =
            fragContainer.findViewById(R.id.autoCompleteTxtViewLocation) as AutoCompleteTextView
        txtInputEdtTxtBirthday = fragContainer.findViewById(R.id.txtInputEdtTxtBirthday)
        imgViewEditProfilePic = fragContainer.findViewById(R.id.imgViewEditProfilePic)
        btnAddProfilePic = fragContainer.findViewById(R.id.btnAddProfilePic)
        btnSubmit = fragContainer.findViewById(R.id.btnSubmit)
        txtInputEdtTxtDisplayName = fragContainer.findViewById(R.id.txtInputEdtTxtDisplayName)
        txtInputEdtTxtAboutMe = fragContainer.findViewById(R.id.txtInputEdtTxtAboutMe)
        txtInputEdtTxtRealName = fragContainer.findViewById(R.id.txtInputEdtTxtRealName)
        txtInputEdtTxtOccupation = fragContainer.findViewById(R.id.txtInputEdtTxtOccupation)
        txtInputEdtTxtHeight = fragContainer.findViewById(R.id.txtInputEdtTxtHeight)

        txtInputEdtTxtBirthday.setOnClickListener { displayDatePicker() }
        btnAddProfilePic.setOnClickListener { pickImage() }
        btnSubmit.setOnClickListener { submit() }

    }

    private fun setObservers() {
        viewModel.questionSingleChoices.observe(this, Observer { addSingleChoiceQuestions(it) })
        viewModel.questionLocationsStrings.observe(this, Observer {
            context?.let { context ->
                val adapter = ArrayAdapter(
                    context,
                    android.R.layout.simple_dropdown_item_1line,
                    it
                )
                autoCompleteTxtViewLocation.setAdapter(adapter)
            }
        })
        viewModel.birthday.observe(this, Observer { txtInputEdtTxtBirthday.setText(it) })
        viewModel.profilePicture.observe(this, Observer {
            imgViewEditProfilePic.setImageDrawable(bitmapToRoundDrawable(it))
        })
    }

    private fun addSingleChoiceQuestions(questionsMap: HashMap<String, List<SingleChoiceAnswerItem>>) {
        singleChoiceItemsContainer.removeAllViews()
        context?.let { context ->
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            for (questionsKey in questionsMap.keys) {
                val view = layoutInflater.inflate(R.layout.single_choice_layout, null)
                val txtInputLayout = view.findViewById<TextInputLayout>(R.id.txtInputLayoutSingleSelection)
                val txtViewInput = view.findViewById<TextInputEditText>(R.id.txtInputEdtTxtSingleSelection)

                txtInputLayout.hint = questionsKey
                singleChoiceTextInputs[questionsKey] = txtViewInput

                singleChoiceItemsContainer.addView(view)

                txtViewInput.setOnClickListener {
                    questionsMap[questionsKey]?.let { answers ->
                        val answersStringList = answers.map { it.name }
                        val index = if (txtViewInput.text == null)
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

    private fun displayDatePicker() {
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)
        // date picker dialog
        context?.let { context ->
            val picker = DatePickerDialog(
                context,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    viewModel.setNewBirthday(year, monthOfYear, dayOfMonth)
                },
                currentYear,
                currentMonth,
                currentDay
            )
            picker.show()
        }
    }

    private fun pickImage() {
        val intent = Intent(
            Intent.ACTION_GET_CONTENT,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            val imageUri = intent?.data
            imageUri?.let {
                viewModel.setProfilePicture(uriToBitmap(it))
            }
        }

    }

    private fun uriToBitmap(uri: Uri): Bitmap {
        return MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
    }

    private fun bitmapToRoundDrawable(bitmap: Bitmap): Drawable {
        val imageDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
        imageDrawable.isCircular = true
        imageDrawable.cornerRadius = min(bitmap.width, bitmap.height) / 2.0f

        return imageDrawable
    }

    private fun submit() {
        var height = -1
        txtInputEdtTxtHeight.text?.let {
            if (it.isNotEmpty())
                height = Integer.parseInt(it.toString())
        }

        val displayName = txtInputEdtTxtDisplayName.text.toString()
        val realName = txtInputEdtTxtRealName.text.toString()
        val occupation = txtInputEdtTxtOccupation.text.toString()
        val aboutMe = txtInputEdtTxtAboutMe.text.toString()
        val city = autoCompleteTxtViewLocation.text.toString()
        val answers = HashMap<String, String>()
        for (singleChoiceTextInputsKey in singleChoiceTextInputs.keys) {
            singleChoiceTextInputs[singleChoiceTextInputsKey]?.let {
                answers[singleChoiceTextInputsKey] = it.text.toString()
            }
        }
        viewModel.submit(displayName, realName, occupation, aboutMe, city, height, answers)
    }
}

