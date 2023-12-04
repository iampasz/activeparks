package com.app.activeparks.ui.registration.fragments.registration

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.activeparks.ui.registration.RegistrationViewModel
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentRegistrationBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * Created by O.Dziuba on 21.11.2023.
 */
class RegistrationFragment : Fragment() {

    lateinit var binding: FragmentRegistrationBinding
    private lateinit var callbackManager: CallbackManager
    private val viewModel: RegistrationViewModel by activityViewModel()

    private val imageList = listOf(
        R.drawable.ic_exercises_1,
        R.drawable.ic_exercises_2,
        R.drawable.ic_exercises_3
    )

    private var currentIndex = 0
    private val delayMillis: Long = 752

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
        showNextImage()

        initFacebook()
    }

    private fun initFacebook() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {

                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                val s = "cancel"
            }

            override fun onError(error: FacebookException) {
                val s = error
            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.e("!@#!@#", "handleFacebookAccessToken: ${token.token}")
        viewModel.additionData.facebookToken = token.token
    }

    private fun setListener() {
        with(binding) {
            btnAuthorization.setOnClickListener {
                findNavController().navigate(R.id.action_fRegistration_to_authorizationFragment)
            }
            btnRegistration.setOnClickListener {
                findNavController().navigate(R.id.action_fRegistration_to_registrationUserDataFragment)
            }

            btnFacebook.setOnClickListener {
                LoginManager.getInstance().logInWithReadPermissions(requireActivity()
                    , listOf("public_profile", "email"))
            }
        }
    }

    private fun showNextImage() {
        binding.ivMain1.setImageResource(imageList[currentIndex])
        currentIndex = (currentIndex + 1) % imageList.size
        Handler(Looper.getMainLooper()).postDelayed({ showNextImage() }, delayMillis)
    }
}