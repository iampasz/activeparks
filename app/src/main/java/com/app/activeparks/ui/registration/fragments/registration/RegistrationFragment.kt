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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
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

    private val delayMillis: Long = 1752
    private val rcSignIn = 9001
    private var googleSignInClient: GoogleSignInClient? = null

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
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.btnGoogle.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        googleSignInClient?.let {
            val signInIntent: Intent = it.signInIntent
            startActivityForResult(signInIntent, rcSignIn)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        data?.let {
            if (requestCode == rcSignIn) {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            viewModel.additionData.googleToken = account?.id ?: ""
            viewModel.email = account?.email ?: ""
            findNavController().navigate(R.id.action_fRegistration_to_registrationUserDataFragment)
        } catch (e: ApiException) {
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

            tvLate.setOnClickListener {
                requireActivity().finish()
            }
        }
    }

    private fun showNextImage() {
        Handler(Looper.getMainLooper()).postDelayed(
            { binding.ivMain1.setImageResource(imageList[1]) },
            delayMillis
        )
        Handler(Looper.getMainLooper()).postDelayed(
            { binding.ivMain1.setImageResource(imageList[2]) },
            delayMillis*2
        )
    }
}