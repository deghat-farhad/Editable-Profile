package com.farhad.sparkeditableprofile.updateProfile.view

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navOptions
import com.farhad.sparkeditableprofile.PREFS_NAME
import com.farhad.sparkeditableprofile.PROFILE_ID_KEY
import com.farhad.sparkeditableprofile.R
import com.farhad.sparkeditableprofile.di.DaggerViewModelComponent
import com.farhad.sparkeditableprofile.di.ViewModelFactory
import com.farhad.sparkeditableprofile.model.ProfileItem
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

class FragUpdateProfile : Fragment() {

    val args: FragUpdateProfileArgs by navArgs()


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: UpdateProfileViewModel

    private lateinit var singleChoiceItemsContainer: LinearLayout
    private var singleChoiceTextInputs = HashMap<String, TextInputEditText>()
    private lateinit var autoCompleteTxtViewLocation: AutoCompleteTextView
    private lateinit var imgViewEditProfilePic: ImageView
    private lateinit var btnAddProfilePic: ImageButton
    private lateinit var btnSubmit: Button
    private var isUpdateView = false

    private lateinit var txtInputEdtTxtBirthday: TextInputEditText

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

        if (!::viewModelFactory.isInitialized) {
            try {
                injectThisToDagger(args.profile)
                isUpdateView = true
            } catch (e: Exception) {
                injectThisToDagger(null)
            }
        }
        initViews(view)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UpdateProfileViewModel::class.java)
        setObservers()
    }

    private fun injectThisToDagger(profile: ProfileItem?) {
        DaggerViewModelComponent
            .builder()
            .profileItem(profile)
            .notAValidCity(getString(R.string.invalidLocation))
            .emptyFieldMessage(getString(R.string.emptyIsNotAllowed))
            .youngerThanMessage(getString(R.string.youngerThan18))
            .tooLongMessage(getString(R.string.tooLongError))
            .charactersNotAllowedMessage(getString(R.string.charactersNotAllowed))
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
        setToolbarTitle()

        if (isUpdateView) {
            txtInputEdtTxtHeight.isFocusable = false
            txtInputEdtTxtHeight.isEnabled = false
        }
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
        viewModel.profileRegistered.observe(this, Observer {
            context?.let {context ->
                val settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                val editor = settings.edit()
                editor.putString(PROFILE_ID_KEY , it)
                editor.apply()
            }
            navigateToViewProfile()
        })
        viewModel.profileUpdated.observe(this, Observer {
            findNavController().navigate(R.id.action_fragUpdateProfile_to_fragViewProfile)
        })

        viewModel.displayName.observe(this, Observer { txtInputEdtTxtDisplayName.setText(it) })
        viewModel.realName.observe(this, Observer { txtInputEdtTxtRealName.setText(it) })
        viewModel.occupation.observe(this, Observer { txtInputEdtTxtOccupation.setText(it) })
        viewModel.height.observe(this, Observer { })
        viewModel.aboutMe.observe(this, Observer { txtInputEdtTxtAboutMe.setText(it) })
        viewModel.location.observe(this, Observer { autoCompleteTxtViewLocation.setText(it) })
        viewModel.height.observe(this, Observer { txtInputEdtTxtHeight.setText(it) })

        viewModel.answers.observe(this, Observer {
            for (question in it.keys) {
                singleChoiceTextInputs[question]?.setText(it[question])
            }
        })

        viewModel.registerIsInProgress.observe(this, Observer {
            disableRegisterButton()
        })

        viewModel.birthDayValidation.observe(this, Observer {
            showValidationIssue(txtInputEdtTxtBirthday, it)
        })

        viewModel.displayNameValidation.observe(this, Observer {
            showValidationIssue(txtInputEdtTxtDisplayName, it)
        })

        viewModel.realNameValidation.observe(this, Observer {
            showValidationIssue(txtInputEdtTxtRealName, it)
        })

        viewModel.aboutMeValidation.observe(this, Observer {
            showValidationIssue(txtInputEdtTxtAboutMe, it)
        })

        viewModel.occupationValidation.observe(this, Observer {
            showValidationIssue(txtInputEdtTxtOccupation, it)
        })

        viewModel.heightValidation.observe(this, Observer {
            showValidationIssue(txtInputEdtTxtHeight, it)
        })

        viewModel.locationValidation.observe(this, Observer {
            showValidationIssue(autoCompleteTxtViewLocation, it)
        })

    }

    private fun setToolbarTitle() {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.editProfile)
    }

    private fun navigateToViewProfile(){
        val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
        findNavController().popBackStack(R.id.fragUpdateProfile, true)
        findNavController().navigate(R.id.fragViewProfile, null, options)
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
                if(innerSelectedItem >= 0)
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
            picker.datePicker.maxDate = System.currentTimeMillis()
            picker.show()
        }
    }

    private fun pickImage() {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            val imageUri = intent?.data
            imageUri?.let {uri->
                val inputStream = activity?.contentResolver?.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                context?.let{
                    viewModel.setProfilePicture(bitmap, it.cacheDir.path)
                }
            }
        }
    }

    private fun bitmapToRoundDrawable(bitmap: Bitmap): Drawable {
        val imageDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
        imageDrawable.isCircular = true
        imageDrawable.cornerRadius = min(bitmap.width, bitmap.height) / 2.0f

        return imageDrawable
    }

    private fun disableRegisterButton() {
        btnSubmit.text = getString(R.string.submitting)
        btnSubmit.isEnabled = false
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

    private fun showValidationIssue(view: View, message: String) {
        val viewParent = view.parent.parent
        if (viewParent is TextInputLayout) {
            if (message.isEmpty())
                viewParent.error = null
            else
                viewParent.error = message
        }
    }
}

