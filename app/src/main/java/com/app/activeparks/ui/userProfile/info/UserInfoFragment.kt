package com.app.activeparks.ui.userProfile.info

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.app.activeparks.MainActivity
import com.app.activeparks.ui.userProfile.edit.EditProfileFragment
import com.app.activeparks.ui.userProfile.model.PhotoType
import com.app.activeparks.util.cropper.CropImage
import com.app.activeparks.util.extention.DataHelper
import com.app.activeparks.util.extention.FileHelper
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.setSex
import com.app.activeparks.util.extention.visible
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentUserInfoBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by O.Dziuba on 08.01.2024.
 */
class UserInfoFragment : Fragment() {

    private lateinit var binding: FragmentUserInfoBinding
    private val viewModel: UserInfoViewModel by viewModel()

    private var photoURI: Uri? = null
    private var currentPhotoPath = ""
    private var photoType = PhotoType.AVATAR

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
                        binding.ivLogoUser.gone()
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserInfoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
        observe()

        viewModel.getUser()
    }

    @SuppressLint("SetTextI18n")
    private fun observe() {
        with(viewModel) {
            userDate.observe(viewLifecycleOwner) {
                it?.let {

                    getRole(it.roleId)

                    with(binding) {
                        Glide.with(requireContext())
                            .load(it.photo)
                            .error(R.drawable.ic_prew)
                            .into(ivUser)

                        if (!it.imageBackground.isNullOrEmpty()) {
                            with(binding) {
                                Glide.with(requireContext())
                                    .load(it.imageBackground)
                                    .error(R.drawable.ic_prew)
                                    .into(vBackgroundUser)
                            }
                            ivLogoUser.gone()
                        } else {
                            ivLogoUser.visible()
                        }

                        tvPhoneNumber.text = it.phone
                        tvEmailNumber.text = it.email
                        tvWeigh.text = requireContext().getString(
                            R.string.tv_weight_picker,
                            it.weight.toString()
                        )
                        tvHeight.text = requireContext().getString(
                            R.string.tv_height_picker,
                            it.height.toString()
                        )

                        tvBDay.text = DataHelper.formatBDay(it.birthday ?: "")

                        if (it.aboutMe.isNullOrEmpty()) {
                            tvAboutMeInfo.gone()
                        } else {
                            tvAboutMeInfo.visible()
                            tvAboutMe.text = it.aboutMe
                        }

                        tvSex.text = it.sex?.setSex()

                        tvUserName.text = "${it.firstName} ${it.lastName}"
                    }
                }
            }

            userRole.observe(viewLifecycleOwner) {
                it?.let { binding.tvUserRole.text = it }
            }
        }
    }

    private fun setListener() {
        with(binding) {
            ivBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
            ivAddUser.setOnClickListener {
                setCoverImageDialog()
                photoType = PhotoType.AVATAR
            }
            ivUser.setOnClickListener {
                setCoverImageDialog()
                photoType = PhotoType.AVATAR
            }
            ivAddUserBack.setOnClickListener {
                setCoverImageDialog()
                photoType = PhotoType.BACKGROUND
            }
            ivEditProfile.setOnClickListener {
                openFragment(EditProfileFragment())
            }

            FileHelper.changeSize(vBackgroundUser, resources)
            FileHelper.changeSize(vBackgroundUser, ivUser)
            FileHelper.changeSizeCircle(ivUser, ivUserCircle)
        }
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
    private fun openFragment(fragment: Fragment) {
        (requireActivity() as? MainActivity)?.openFragment(fragment)
    }
}