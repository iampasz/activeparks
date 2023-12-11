@file:Suppress("DEPRECATION")

package com.app.activeparks.ui.registration.fragments.registration

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.activeparks.ui.registration.RegistrationViewModel
import com.app.activeparks.util.EasyFacebookCallback
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.GoogleApiClient
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentRegistrationBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * Created by O.Dziuba on 21.11.2023.
 */
@Suppress("DEPRECATION")
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
    private val rcSignIn = 9001
    private var mGoogleApiClient: GoogleApiClient? = null
    private var clientId = "97103190835-c98mket2dgt5e849h870jqrulba3p8fa.apps.googleusercontent.com"

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
        initGoogle()
    }

    private fun initGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId)
            .requestEmail()
            .build()

        mGoogleApiClient = GoogleApiClient.Builder(requireActivity())
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

        binding.btnGoogle.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        mGoogleApiClient?.let {
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(it)
            startActivityForResult(signInIntent, rcSignIn)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        data?.let {
            if (requestCode == rcSignIn) {
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(it)
                result?.apply {
                    handleSignInResult(this)
                }
            }
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            viewModel.additionData.googleToken = result.signInAccount?.idToken ?: ""
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.tv_somsing_went_wrong), Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun initFacebook() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, object :
            EasyFacebookCallback() {
            override fun onSuccess(loginResult: LoginResult) {
                handleFacebookAccessToken(loginResult.accessToken)
            }
        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
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
                LoginManager.getInstance().logInWithReadPermissions(
                    requireActivity(), listOf("public_profile", "email")
                )
            }
        }
    }

    private fun showNextImage() {
        binding.ivMain1.setImageResource(imageList[currentIndex])
        currentIndex = (currentIndex + 1) % imageList.size
        Handler(Looper.getMainLooper()).postDelayed({ showNextImage() }, delayMillis)
    }
}