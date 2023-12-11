package com.app.activeparks.ui.registration.fragments.registrationFlow.fragments.userData

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.activeparks.ui.registration.RegistrationViewModel
import com.app.activeparks.util.EasyTextWatcher
import com.app.activeparks.util.MIN_PASSWORD_LENGTH
import com.app.activeparks.util.extention.disable
import com.app.activeparks.util.extention.enableIf
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.isNotEmpty
import com.app.activeparks.util.extention.visible
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentRegistrationUserDataBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * Created by O.Dziuba on 23.11.2023.
 */
class RegistrationUserDataFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationUserDataBinding
    private val viewModel: RegistrationViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationUserDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        observes()
    }

    private fun observes() {
        with(viewModel) {
            sendSmsCodeLD.observe(viewLifecycleOwner) {
                binding.progress.gone()
                if (it == true) {
                    findNavController().navigate(R.id.action_registrationUserDataFragment_to_verificationFragment)
                    sendSmsCodeLD.value = false
                }
            }
            onHideProgress.observe(viewLifecycleOwner) {
                binding.progress.gone()
            }
        }
    }

    private fun initListener() {
        with(binding) {
            phoneEditText.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    btnNext.enableIf(isDataValid())
                    viewModel.sendCodePhoneRequest.phone = s.toString()
                }
            })
            nickEditText.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    btnNext.enableIf(isDataValid())
                    viewModel.sendCodePhoneRequest.nickname = s.toString()
                }
            })
            passwordEditText.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    btnNext.enableIf(isDataValid())
                }
            })
            rePasswordEditText.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    btnNext.enableIf(isDataValid())
                    viewModel.sendCodePhoneRequest.password = s.toString()
                }
            })

            btnNext.setOnClickListener {
                btnNext.disable()
                progress.visible()
                viewModel.sendCodePhone()
            }
            btnBack.setOnClickListener {
                requireActivity().onBackPressed()
                progress.gone()
            }
        }
    }

    private fun isDataValid() =
        binding.phoneEditText.isNotEmpty() &&
                binding.nickEditText.isNotEmpty() &&
                (binding.passwordEditText.isNotEmpty() && isPasswordValid(
                    binding.passwordEditText.text.toString(),
                    binding.rePasswordEditText.text.toString()
                ))


    private fun isPasswordValid(password: String, confirmPassword: String): Boolean {
        if (password.length < MIN_PASSWORD_LENGTH) {
            return false
        }

        val containsDigit = password.any { it.isDigit() }
        val containsUpperCase = password.any { it.isUpperCase() }
        val containsLowerCase = password.any { it.isLowerCase() }
        val isPasswordValid = containsDigit && containsUpperCase && containsLowerCase
        val isConfirmationMatch = password == confirmPassword

        return isPasswordValid && isConfirmationMatch
    }
}
