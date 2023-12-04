package com.app.activeparks.ui.registration.fragments.registrationFlow.fragments.verificationEmail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.activeparks.ui.registration.RegistrationViewModel
import com.app.activeparks.ui.view.SmsCodeInputView
import com.app.activeparks.util.extention.disable
import com.app.activeparks.util.extention.enable
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentVerificationEmailBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * Created by O.Dziuba on 23.11.2023.
 */
class VerificationEmailFragment : Fragment() {

    lateinit var binding: FragmentVerificationEmailBinding
    private val viewModel: RegistrationViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerificationEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListener()
        observe()
    }

    private fun observe() {
        with(viewModel) {
            verificationEmailCodeLD.observe(viewLifecycleOwner) {
                if (it == true) {
                    findNavController().navigate(R.id.action_fVerificationEmail_to_fAdditionalValue)
                }
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
                viewModel.verificationEmailCode()
            }
            btnBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }
}