package com.app.activeparks.ui.registration.fragments.registrationFlow.fragments.verificationPhone

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.activeparks.ui.registration.RegistrationViewModel
import com.app.activeparks.ui.view.SmsCodeInputView
import com.app.activeparks.util.extention.disable
import com.app.activeparks.util.extention.enable
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.visible
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentVerificationPhoneBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * Created by O.Dziuba on 23.11.2023.
 */
class VerificationPhoneFragment : Fragment() {

    lateinit var binding: FragmentVerificationPhoneBinding
    private val viewModel: RegistrationViewModel by activityViewModel()

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
        binding = FragmentVerificationPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        countDownTimer.start()
        setListener()
        observe()
    }

    private fun observe() {
        with(viewModel) {
            verificationSmsCodeLD.observe(viewLifecycleOwner) {
                binding.progress.gone()
                if (it == true) {
                    findNavController().navigate(R.id.action_verificationFragment_to_additionalDataFragment)
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
                    viewModel.code = code
                    btnNext.enable()
                }

                override fun notCodeComplete() {
                    btnNext.disable()
                }
            })

            btnNext.setOnClickListener {
                btnNext.disable()
                progress.visible()
                countDownTimer.cancel()
                viewModel.verificationSmsCode()
            }
            btnBack.setOnClickListener {
                progress.gone()
                requireActivity().onBackPressed()
            }
            tvHelpSms.setOnClickListener {
                viewModel.sendCodePhone()
                countDownTimer.start()
                tvSmsTimerTitle.visible()
                tvHelpSms.disable()
            }
        }
    }
}