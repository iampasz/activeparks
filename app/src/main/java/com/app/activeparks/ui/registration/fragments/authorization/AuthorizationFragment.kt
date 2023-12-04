package com.app.activeparks.ui.registration.fragments.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentAuthorizationBinding

/**
 * Created by O.Dziuba on 22.11.2023.
 */
class AuthorizationFragment : Fragment() {

    lateinit var binding: FragmentAuthorizationBinding

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
    }

    private fun setListener() {
        with(binding) {
            tvForgotPassword.setOnClickListener {
                findNavController().navigate(R.id.action_fAuthorization_to_fForgotPassword)
            }

            btnBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }
}