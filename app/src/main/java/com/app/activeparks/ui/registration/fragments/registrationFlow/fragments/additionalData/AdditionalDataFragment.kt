package com.app.activeparks.ui.registration.fragments.registrationFlow.fragments.additionalData

import android.os.Bundle
import android.text.Editable
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.activeparks.ui.registration.RegistrationViewModel
import com.app.activeparks.util.EasyTextWatcher
import com.app.activeparks.util.extention.disable
import com.app.activeparks.util.extention.enableIf
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.isNotEmpty
import com.app.activeparks.util.extention.visible
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentAdditionalDataBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * Created by O.Dziuba on 23.11.2023.
 */
class AdditionalDataFragment : Fragment() {

    lateinit var binding: FragmentAdditionalDataBinding
    private val viewModel: RegistrationViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdditionalDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListener()
        observe()
    }

    private fun observe() {
        with(viewModel) {
            sendEmailCodeLD.observe(viewLifecycleOwner) {
                binding.progress.gone()
                if (it == true) findNavController().navigate(R.id.action_fAdditionalData_to_fVerificationEmail)
            }
            onHideProgress.observe(viewLifecycleOwner) {
                binding.progress.gone()
            }
        }
    }

    private fun setListener() {
        with(binding) {
            if (viewModel.email.isNotEmpty()) {
                emailEditText.setText(viewModel.email)
            }
            emailEditText.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    btnNext.enableIf(isDataValid())
                    viewModel.email = s.toString()
                }
            })
            fNameEditText.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    btnNext.enableIf(isDataValid())
                    viewModel.additionData.fName = s.toString()
                }
            })
            lNameEditText.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    btnNext.enableIf(isDataValid())
                    viewModel.additionData.lName = s.toString()
                }
            })
            btnNext.setOnClickListener {
                btnNext.disable()
                progress.visible()
                viewModel.sendCodeEmail()
            }
            btnBack.setOnClickListener {
                requireActivity().onBackPressed()
                progress.gone()
            }
            btnSkip.setOnClickListener {
                findNavController().navigate(R.id.action_fAdditionalData_to_fAdditionalValue)
            }
        }
    }


    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    private fun isDataValid() =
        isEmailValid(binding.emailEditText.text.toString()) &&
                binding.fNameEditText.isNotEmpty() &&
                binding.lNameEditText.isNotEmpty()


}