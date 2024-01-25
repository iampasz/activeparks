package com.app.activeparks.ui.userProfile.edit

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.app.activeparks.ui.registration.fragments.registrationFlow.fragments.additionalValue.Gender
import com.app.activeparks.ui.userProfile.model.PhotoType
import com.app.activeparks.util.EasyTextWatcher
import com.app.activeparks.util.PhoneNumberMaskWatcher
import com.app.activeparks.util.cropper.CropImage
import com.app.activeparks.util.extention.DataHelper
import com.app.activeparks.util.extention.FileHelper
import com.app.activeparks.util.extention.replacePhone
import com.app.activeparks.util.extention.setSex
import com.app.activeparks.util.extention.toBoolean
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentEditProfileBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Created by O.Dziuba on 09.01.2024.
 */
class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModel()

    private var photoURI: Uri? = null
    private var currentPhotoPath = ""
    private var photoType = PhotoType.AVATAR
    private val calendar = Calendar.getInstance()
    private val dateFormatUI = SimpleDateFormat("d MMMM yyyy", Locale("uk", "UA"))
    private val dateFormatBack = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private val getContentLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            val ratioWidth = 3
            val ratioHeight = 2
            val cropIntent = CropImage.activity(uri)
                .setAspectRatio(ratioWidth, ratioHeight)
                .getIntent(requireContext())
            cropActivityResultLauncher.launch(cropIntent)
        }
    private val takePictureLauncherAgain =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    val imageUri = photoURI

                    val ratioWidth = 3
                    val ratioHeight = 2
                    val cropIntent = CropImage.activity(imageUri)
                        .setAspectRatio(ratioWidth, ratioHeight)
                        .getIntent(requireContext())
                    cropActivityResultLauncher.launch(cropIntent)
                }
            }
        }
    private val cropActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                data?.let {
                    val resultUri = CropImage.getActivityResult(data).uri
                    FileHelper.uriToFile(resultUri, requireContext())?.let {
                        viewModel.updateImg(it, photoType)
                    }

                    if (photoType == PhotoType.AVATAR) {
                        Glide.with(requireContext())
                            .load(resultUri)
                            .error(R.drawable.ic_prew)
                            .into(binding.ivUser)
                    } else {
                        Glide.with(requireContext())
                            .load(resultUri)
                            .error(R.drawable.ic_prew)
                            .into(binding.vBackgroundUser)
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
        observe()

        viewModel.getUser()
    }

    private fun observe() {
        with(viewModel) {
            userDate.observe(viewLifecycleOwner) {
                it?.let {
                    with(binding) {
                        Glide.with(requireContext())
                            .load(it.photo)
                            .error(R.drawable.ic_prew)
                            .into(ivUser)
                        Glide.with(requireContext())
                            .load(it.imageBackground)
                            .error(R.drawable.ic_prew)
                            .into(vBackgroundUser)

                        etFName.setText(it.firstName)
                        etLName.setText(it.lastName)
                        etAboutMe.setText(it.aboutMe)

                        etCity.setText(it.city)
                        etBDay.text = DataHelper.formatBDay(it.birthday ?: "")
                        etSex.text = it.sex?.setSex()

                        etWeight.text = getString(R.string.tv_weight_picker, it.weight.toString())
                        etHeight.text = getString(R.string.tv_height_picker, it.height.toString())

                        scVeteran.isChecked = it.isVeteran.toBoolean()
                        scVPO.isChecked = it.isVpo.toBoolean()
                        scOpenInfo.isChecked = it.hideBodyInfo.toBoolean()

                        etPhone.setText(it.phone)
                        etEmail.setText(it.email)
                    }
                }
            }

            userSaved.observe(viewLifecycleOwner) {
                if (it == true) {
                    onBackPressed()
                }
            }
        }
    }

    private fun setListener() {
        with(binding) {
            changeSize()

            ivBack.setOnClickListener {
                onBackPressed()
            }
            ivAddUser.setOnClickListener {
                photoType = PhotoType.AVATAR
                setCoverImageDialog()
            }
            ivUser.setOnClickListener {
                photoType = PhotoType.AVATAR
                setCoverImageDialog()
            }
            ivAddUserBack.setOnClickListener {
                photoType = PhotoType.BACKGROUND
                setCoverImageDialog()
            }

            etWeight.setOnClickListener {
                showWeightPicker()
            }
            etHeight.setOnClickListener {
                showHeightPicker()
            }
            etBDay.setOnClickListener {
                showDatePicker()
            }
            etSex.setOnClickListener {
                showGenderOptions()
            }

            etFName.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.user = viewModel.user?.copy(firstName = s?.toString())
                }
            })

            etLName.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.user = viewModel.user?.copy(lastName = s?.toString())
                }
            })

            etAboutMe.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.user = viewModel.user?.copy(aboutMe = s?.toString())
                }
            })

            etCity.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.user = viewModel.user?.copy(city = s?.toString())
                }
            })

            etPhone.addTextChangedListener(PhoneNumberMaskWatcher(etPhone) {
                viewModel.user = viewModel.user?.copy(phone = etPhone.text?.toString()?.replacePhone())
            })

            etEmail.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.user = viewModel.user?.copy(email = s?.toString())
                }
            })

            btnSave.setOnClickListener {
                viewModel.saveUser()
            }

            ivSave.setOnClickListener {
                viewModel.saveUser()
            }

            scVPO.setOnCheckedChangeListener { _, isChecked ->
                viewModel.user = viewModel.user?.copy(isVpo = if (isChecked) 1 else 0)
            }

            scVeteran.setOnCheckedChangeListener { _, isChecked ->
                viewModel.user = viewModel.user?.copy(isVeteran = if (isChecked) 1 else 0)
            }

            scOpenInfo.setOnCheckedChangeListener { _, isChecked ->
                viewModel.user = viewModel.user?.copy(hideBodyInfo = if (isChecked) 1 else 0)
            }
        }
    }

    private fun onBackPressed() {
        requireActivity().onBackPressed()
    }

    private fun showWeightPicker() {
        val weightPicker = NumberPicker(requireContext())

        val minValue = 40100
        val maxValue = 200000
        val step = 100

        val valueCount = (maxValue - minValue) / step + 1
        val displayedValues = Array(valueCount) { i ->
            val weight = minValue + i * step
            (weight / 1000).toString() + "." + (weight % 1000)
        }

        weightPicker.minValue = 0
        weightPicker.maxValue = valueCount - 1
        weightPicker.displayedValues = displayedValues


        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.tv_select_weight))
            .setView(weightPicker)
            .setPositiveButton(getString(R.string.tv_ok)) { _, _ ->

                val selectedWeight = minValue + weightPicker.value * step
                val formattedWeight =
                    (selectedWeight / 1000).toString() + "." + (selectedWeight % 1000)

                binding.etWeight.text =
                    getString(R.string.tv_weight_picker, formattedWeight)

                viewModel.user = viewModel.user?.copy(weight = formattedWeight.toDouble())
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
                binding.etHeight.text =
                    getString(R.string.tv_height_picker, numberPicker.value.toString())
                viewModel.user = viewModel.user?.copy(height = numberPicker.value)
            }
            .create()

        dialog.show()
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
                binding.etBDay.text = formattedDateUI
                with(viewModel) {
                    viewModel.user = viewModel.user?.copy(birthday = formattedDateBack)
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

    private fun showGenderOptions() {
        val popupMenu = PopupMenu(requireContext(), binding.etSex)

        val genders = Gender.getGenders()
        genders.forEach { gender ->
            popupMenu.menu.add(gender).setOnMenuItemClickListener {
                binding.etSex.text = gender
                val sex = when (gender) {
                    genders.first() -> Gender.MALE
                    else -> Gender.FEMALE
                }


                viewModel.user = viewModel.user?.copy(sex = sex)
                true
            }
        }

        popupMenu.show()
    }

    private fun calculateFullYears(birthDate: Int): Int {
        return Calendar.getInstance().get(Calendar.YEAR) - birthDate
    }

    private fun FragmentEditProfileBinding.changeSize() {
        FileHelper.changeSize(vBackgroundUser, resources)
        FileHelper.changeSize(vBackgroundUser, ivUser)
        FileHelper.changeSizeCircle(ivUser, ivUserCircle)
    }


    @SuppressLint("QueryPermissionsNeeded")
    private fun setCoverImageDialog() {
        val dialog = BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
        dialog.setContentView(R.layout.dialog_gallery)
        val galleryAction = dialog.findViewById<LinearLayout>(R.id.gallery_action)
        galleryAction?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                getContentLauncher.launch("image/*")
            } else {
                Toast.makeText(
                    activity,
                    getString(R.string.add_access_to_photo),
                    Toast.LENGTH_SHORT
                )
                    .show()
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            }
            dialog.dismiss()
        }

        val cameraAction = dialog.findViewById<LinearLayout>(R.id.camera_action)!!
        cameraAction.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.CAMERA
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(
                    activity,
                    getString(R.string.add_access_to_camera),
                    Toast.LENGTH_SHORT
                )
                    .show()
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    5
                )
            }
            dialog.dismiss()
        }

        val cancel = dialog.findViewById<LinearLayout>(R.id.cancel)!!
        cancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    @SuppressLint("QueryPermissionsNeeded")
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.android.fileprovider",
                        it
                    )

                    this.photoURI = photoURI
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    takePictureLauncherAgain.launch(takePictureIntent)
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }
}