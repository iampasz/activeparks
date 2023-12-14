package com.app.activeparks.ui.registration.fragments.forgotPassword

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.MainActivity
import com.app.activeparks.ui.view.SmsCodeInputView
import com.app.activeparks.util.EasyTextWatcher
import com.app.activeparks.util.MIN_PASSWORD_LENGTH
import com.app.activeparks.util.extention.Keyboard
import com.app.activeparks.util.extention.disable
import com.app.activeparks.util.extention.enable
import com.app.activeparks.util.extention.enableIf
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.isPhone
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
    private var countDownTimer: CountDownTimer =
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvSmsTimerTitle.text =
                    getString(R.string.tv_sms_timer, (millisUntilFinished / 1000).toString())
            }

            override fun onFinish() {
                binding.tvSmsTimerTitle.gone()
                binding.tvHelpSms.enable()
            }
        }

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
        observe()
    }

    private fun observe() {
        with(viewModel) {
            updateUI.observe(viewLifecycleOwner) {
                if (it == true) {
                    with(binding) {
                        viewModel.state.apply {
                            progress.gone()
                            if (isSms && !isPassword) {
                                gEnterEmail.gone()
                                gEnterSms.visible()
                                btnNext.disable()
                                tvTitleSms.text = if (typeOfForgot == TypeOfForgot.EMAIl) {
                                    getString(R.string.tv_enter_email)
                                } else {
                                    getString(R.string.tv_enter_sms)
                                }
                                Keyboard.hideKeyboard(requireContext(), requireView())
                            } else if (isSms && !isComplete) {
                                btnNext.disable()
                                gEnterSms.gone()
                                gEnterPassword.visible()
                                Keyboard.hideKeyboard(requireContext(), requireView())
                                btnNext.text = getString(R.string.tv_to_enter)
                            } else if (isComplete) {
                                startActivity(Intent(requireActivity(), MainActivity::class.java))
                                requireActivity().finish()
                            }
                        }
                    }
                }
            }

            onHideProgress.observe(viewLifecycleOwner) {
                binding.progress.gone()
            }
        }
    }

    private fun setListener() {
        with(binding) {
            smsCodeInputView.setCodeCompleteListener(object :
                SmsCodeInputView.CodeCompleteListener {
                override fun onCodeComplete(code: String) {
                    btnNext.enable()
                    viewModel.apply {
                        verificationCodeForgotPasswordRequest.userCode = code
                        resetPasswordResponse.userCode = code
                    }
                }

                override fun notCodeComplete() {
                    btnNext.disable()
                }

            })

            btnNext.setOnClickListener {
                viewModel.state.apply {
                    btnNext.disable()
                    progress.visible()
                    if (isEmail && (!isSms && !isPassword)) {
                        sendCode()
                    } else if (isSms && !isPassword) {
                        viewModel.verificationCode()
                        countDownTimer.cancel()
                    } else if (isEmail && isSms) {
                        viewModel.resetPassword()
                    }
                }
            }

            tvHelpSms.setOnClickListener {
                sendCode()
            }

            emailEditText.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    btnNext.enableIf(s.toString().isNotEmpty())
                    viewModel.apply {
                        val login = s.toString()
                        typeOfForgot = if (isEmail(login)) {
                            TypeOfForgot.EMAIl
                        } else if (login.isPhone()) {
                            TypeOfForgot.PHONE
                        } else {
                            TypeOfForgot.NICK
                        }
                        forgotPasswordRequest.phoneLogin = login
                        verificationCodeForgotPasswordRequest.phoneLogin = login
                        resetPasswordResponse.phoneLogin = login
                    }
                }
            })

            passwordEditText.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    val password = s.toString()
                    btnNext.enableIf(
                        isPasswordValid(
                            password,
                            rePasswordEditText.text.toString()
                        )
                    )
                    viewModel.resetPasswordResponse.password = password

                }
            })

            rePasswordEditText.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    val rePassword = s.toString()
                    btnNext.enableIf(
                        isPasswordValid(
                            rePassword,
                            passwordEditText.text.toString()
                        )
                    )
                    viewModel.resetPasswordResponse.passwordRepeat = rePassword
                }
            })

            btnBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun sendCode() {
        binding.tvSmsTimerTitle.visible()
        binding.tvHelpSms.disable()
        viewModel.forgotPassword()
        countDownTimer.start()
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


    private fun isEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
