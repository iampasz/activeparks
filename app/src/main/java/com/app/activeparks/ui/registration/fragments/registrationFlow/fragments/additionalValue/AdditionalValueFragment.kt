package com.app.activeparks.ui.registration.fragments.registrationFlow.fragments.additionalValue

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.app.activeparks.MainActivity
import com.app.activeparks.ui.registration.RegistrationViewModel
import com.app.activeparks.ui.registration.fragments.registrationFlow.fragments.additionalValue.Gender.Companion.FEMALE
import com.app.activeparks.ui.registration.fragments.registrationFlow.fragments.additionalValue.Gender.Companion.MALE
import com.app.activeparks.util.extention.disable
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.isSelectedToResponse
import com.app.activeparks.util.extention.visible
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentAdditionalValueBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Created by O.Dziuba on 23.11.2023.
 */
class AdditionalValueFragment : Fragment() {

    lateinit var binding: FragmentAdditionalValueBinding
    private val viewModel: RegistrationViewModel by activityViewModel()
    private val calendar = Calendar.getInstance()
    private val dateFormatUI = SimpleDateFormat("d MMMM yyyy", Locale("uk", "UA"))
    private val dateFormatBack = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdditionalValueBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListener()
        observe()
    }

    private fun observe() {
        with(viewModel) {
            onRegistered.observe(viewLifecycleOwner) {
                binding.progress.gone()
                if (it == true) {
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().finish()
                }
            }

            onHideProgress.observe(viewLifecycleOwner) {
                binding.progress.gone()
            }
        }
    }

    private fun setListener() {
        with(binding) {
            btnNext.setOnClickListener {
                btnNext.disable()
                progress.visible()
                viewModel.updateUserData()
            }

            btnWeight.setOnClickListener {
                showWeightPicker()
            }
            btnHeight.setOnClickListener {
                showHeightPicker()
            }
            btnGender.setOnClickListener {
                showGenderOptions()
            }
            btnBirthDate.setOnClickListener {
                showDatePicker()
            }
            btnVpo.setOnClickListener {
                toggleVpoState()
            }
            btnVeteran.setOnClickListener {
                toggleVeteranState()
            }
            btnBack.setOnClickListener {
                progress.gone()
                requireActivity().onBackPressed()
            }
        }
    }


    private fun showWeightPicker() {
        val numberPicker = NumberPicker(requireContext())
        numberPicker.minValue = 40
        numberPicker.maxValue = 200
        numberPicker.value = 90

        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.tv_select_weight))
            .setView(numberPicker)
            .setPositiveButton(getString(R.string.tv_ok)) { _, _ ->
                binding.btnWeight.text =
                    getString(R.string.tv_weight_picker, numberPicker.value.toString())
                viewModel.additionData.weight = numberPicker.value
            }
            .create()

        dialog.show()
    }

    private fun showHeightPicker() {
        val numberPicker = NumberPicker(requireContext())
        numberPicker.minValue = 100
        numberPicker.maxValue = 250
        numberPicker.value = 175

        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.tv_select_height_picker))
            .setView(numberPicker)
            .setPositiveButton(getString(R.string.tv_ok)) { _, _ ->
                binding.btnHeight.text =
                    getString(R.string.tv_height_picker, numberPicker.value.toString())
                viewModel.additionData.height = numberPicker.value
            }
            .create()

        dialog.show()
    }

    private fun showGenderOptions() {
        val popupMenu = PopupMenu(requireContext(), binding.btnGender)

        val genders = Gender.getGenders()
        genders.forEach { gender ->
            popupMenu.menu.add(gender).setOnMenuItemClickListener {
                binding.btnGender.text = gender
                val sex = when (gender) {
                    genders.first() -> MALE
                    else -> FEMALE
                }

                viewModel.additionData.sex = sex
                true
            }
        }

        popupMenu.show()
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = calendar.apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }.time

                val formattedDateUI = dateFormatUI.format(selectedDate)
                val formattedDateBack = dateFormatBack.format(selectedDate)
                binding.btnBirthDate.text = formattedDateUI
                with(viewModel) {
                    additionData.birthday = formattedDateBack
                    calculatePulseZone(calculateFullYears(year))
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun calculateFullYears(birthDate: Int): Int {
        return Calendar.getInstance().get(Calendar.YEAR) - birthDate
    }

    private fun toggleVpoState() {
        binding.btnVpo.isSelected = !binding.btnVpo.isSelected
        viewModel.additionData.isVpo = binding.btnVpo.isSelectedToResponse()
    }

    private fun toggleVeteranState() {
        binding.btnVeteran.isSelected = !binding.btnVeteran.isSelected
        viewModel.additionData.isVeteran = binding.btnVeteran.isSelectedToResponse()
    }
}