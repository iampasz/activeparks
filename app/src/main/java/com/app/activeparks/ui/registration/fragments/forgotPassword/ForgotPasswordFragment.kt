package com.app.activeparks.ui.registration.fragments.forgotPassword

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.activeparks.ui.view.SmsCodeInputView
import com.app.activeparks.util.EasyTextWatcher
import com.app.activeparks.util.MIN_PASSWORD_LENGTH
import com.app.activeparks.util.extention.disable
import com.app.activeparks.util.extention.enable
import com.app.activeparks.util.extention.enableIf
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.visible
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentForgotPasswordBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Created by O.Dziuba on 22.11.2023.
 */
class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding
    private val viewModel: ForgotPasswordViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
    }

    private fun setListener() {
        with(binding) {
            smsCodeInputView.setCodeCompleteListener(object :
                SmsCodeInputView.CodeCompleteListener {
                override fun onCodeComplete(code: String) {
                    btnNext.enable()
                    Toast.makeText(requireContext(), code, Toast.LENGTH_LONG).show()
                }

                override fun notCodeComplete() {
                    btnNext.disable()
                }

            })

            btnNext.setOnClickListener {
                viewModel.state.apply {
                    if (isEmail && (!isSms && !isPassword)) {
                        isSms = true
                        gEnterEmail.gone()
                        gEnterSms.visible()
                        btnNext.disable()
                    } else if (isSms && !isPassword) {
                        btnNext.disable()
                        isPassword = true
                        gEnterSms.gone()
                        gEnterPassword.visible()
                        btnNext.text = getString(R.string.tv_to_enter)
                    } else if (isEmail && isSms) {
                        findNavController().navigate(R.id.action_fForgotPassword_to_fAuthorization)
                    }
                }
            }

            emailEditText.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    btnNext.enableIf(s.toString().isNotEmpty())
                }
            })

            passwordEditText.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    btnNext.enableIf(
                        isPasswordValid(
                            s.toString(),
                            rePasswordEditText.text.toString()
                        )
                    )
                }
            })

            rePasswordEditText.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    btnNext.enableIf(
                        isPasswordValid(
                            s.toString(),
                            passwordEditText.text.toString()
                        )
                    )
                }
            })

            btnBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

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
