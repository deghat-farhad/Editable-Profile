package com.farhad.sparkeditableprofile.viewProfile.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
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
import com.farhad.sparkeditableprofile.PREFS_NAME
import com.farhad.sparkeditableprofile.PROFILE_ID_KEY
import com.farhad.sparkeditableprofile.R
import com.farhad.sparkeditableprofile.di.DaggerViewModelComponent
import com.farhad.sparkeditableprofile.di.ViewModelFactory
import com.farhad.sparkeditableprofile.viewProfile.viewModel.ViewProfileViewModel
import javax.inject.Inject
import kotlin.math.min

class FragViewProfile : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: ViewProfileViewModel

    private lateinit var imgViwProfile: ImageView
    private lateinit var txtViwDisplayName: TextView
    private lateinit var txtViwLocation: TextView
    private lateinit var btnEditProfile: Button
    private lateinit var txtViwAboutMe: TextView
    private lateinit var txtViwBirthday: TextView
    private lateinit var answerContainer: LinearLayout
    private lateinit var txtViwHeight: TextView

    private lateinit var profileContainer: LinearLayout
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(
            R.layout.frag_view_profile,
            container, false
        )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiate(view)
        showProgressBar()

        if (!::viewModelFactory.isInitialized) {
            injectThisToDagger()
        }
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ViewProfileViewModel::class.java)
        setObservers()
        context?.let{context ->
            val settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val profileId: String = settings.getString(PROFILE_ID_KEY, "") ?: ""
            if(profileId.isNotEmpty())
                viewModel.getProfile(profileId)
        }
    }

    private fun initiate(view:View){
        imgViwProfile = view.findViewById(R.id.imgViwProfile)
        txtViwDisplayName = view.findViewById(R.id.txtViwDisplayName)
        txtViwLocation = view.findViewById(R.id.txtViwLocation)
        txtViwAboutMe = view.findViewById(R.id.txtViwAboutMe)
        txtViwHeight = view.findViewById(R.id.txtViwHeight)
        txtViwBirthday = view.findViewById(R.id.txtViwBirthday)
        answerContainer = view.findViewById(R.id.answerContainer)
        btnEditProfile = view.findViewById(R.id.btnEditProfile)
        profileContainer = view.findViewById(R.id.profileContainer)
        progressBar = view.findViewById(R.id.progressBar)

        btnEditProfile.setOnClickListener {
            viewModel.updateProfile()
        }
        setToolbarTitle()
    }

    private fun injectThisToDagger() {
        DaggerViewModelComponent
            .builder()
            .profileItem(null)
            .build()
            .injectFragment(this)
    }

    private fun setObservers() {
        viewModel.displayName.observe(this, Observer {
                displayName -> txtViwDisplayName.text = displayName
            hideProgressBar()
        })
        viewModel.location.observe(this, Observer { location -> txtViwLocation.text = location })
        viewModel.aboutMe.observe(this, Observer { aboutMe -> txtViwAboutMe.text = aboutMe })
        viewModel.birthday.observe(this, Observer { birthday -> txtViwBirthday.text = birthday })
        viewModel.height.observe(this, Observer { height -> txtViwHeight.text = height })

        viewModel.profilePicture.observe(this, Observer {
            imgViwProfile.setImageDrawable(bitmapToRoundDrawable(it))
        })

        viewModel.answers.observe(this, Observer { answersMap ->
            addAnswersToAnswerContainer(answersMap)
        })

        viewModel.navigateToEditProfile.observe(this, Observer {
            val action = FragViewProfileDirections.actionFragViewProfileToFragUpdateProfile(it)

            findNavController().navigate(action)
        })
    }

    private fun setToolbarTitle() {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.profile)
    }

    private fun bitmapToRoundDrawable(bitmap: Bitmap): Drawable {
        val imageDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
        imageDrawable.isCircular = true
        imageDrawable.cornerRadius = min(bitmap.width, bitmap.height) / 2.0f

        return imageDrawable
    }

    private fun addAnswersToAnswerContainer(answersMap: HashMap<String, String>) {
        answerContainer.removeAllViews()
        context?.let { context ->
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            for (key in answersMap.keys) {
                val answerLayout = layoutInflater.inflate(R.layout.answer_layout, null)
                answerLayout.findViewById<TextView>(R.id.txtViwSingleSelectionHeader).text = key
                answerLayout.findViewById<TextView>(R.id.txtViwSingleSelectionValue).text = answersMap[key]
                answerContainer.addView(answerLayout)
            }
        }
    }

    private fun showProgressBar() {
        profileContainer.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }
    private fun hideProgressBar() {
        profileContainer.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }
}