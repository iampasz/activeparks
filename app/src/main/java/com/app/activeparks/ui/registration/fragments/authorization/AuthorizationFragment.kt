package com.app.activeparks.ui.registration.fragments.authorization

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.activeparks.MainActivity
import com.app.activeparks.ui.registration.RegistrationViewModel
import com.app.activeparks.util.EasyTextWatcher
import com.app.activeparks.util.URL_INFO_LIST
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentAuthorizationBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * Created by O.Dziuba on 22.11.2023.
 */
class AuthorizationFragment : Fragment() {

    lateinit var binding: FragmentAuthorizationBinding
    private val viewModel: RegistrationViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
        observe()
    }

    private fun observe() {
        viewModel.onLoggedIn.observe(viewLifecycleOwner) {
            if (it == true) {
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun setListener() {
        with(binding) {
            tvRestoreAccess.setOnClickListener {
                findNavController().navigate(R.id.action_fAuthorization_to_fForgotPassword)
            }
            tvView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(URL_INFO_LIST))
                startActivity(intent)
            }

            btnBack.setOnClickListener {
                requireActivity().onBackPressed()
            }

            emailEditText.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.loginRequest.phoneLogin = s?.toString() ?: ""
                }
            })
            passwordEditText.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.loginRequest.password = s?.toString() ?: ""
                }
            })

            btnEnter.setOnClickListener {
                viewModel.login()
            }
        }
    }
}