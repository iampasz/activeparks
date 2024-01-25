package com.app.activeparks.ui.userProfile.video

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.app.activeparks.ui.userProfile.video.model.VideoSelector
import com.app.activeparks.util.EasyTextWatcher
import com.app.activeparks.util.cropper.CropImage
import com.app.activeparks.util.extention.FileHelper
import com.app.activeparks.util.extention.enableIf
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentUserProfileAddVideoBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by O.Dziuba on 25.12.2023.
 */
class AddVideoUserProfile(
    private val id: String? = null
) : Fragment() {

    private lateinit var binding: FragmentUserProfileAddVideoBinding
    private val viewModel: VideoUserProfileViewModel by viewModel()
    private var photoURI: Uri? = null
    private var currentPhotoPath = ""
    private var videoCategoryList = mutableListOf<VideoSelector>()
    private var videoLevelList = mutableListOf<VideoSelector>()

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
                        viewModel.updateImg(it)
                    }

                    Glide.with(requireContext())
                        .load(resultUri)
                        .error(R.drawable.ic_prew)
                        .into(binding.ivImg)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileAddVideoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getVideoValue()
        if (!id.isNullOrEmpty()) {
            viewModel.getUserVideo(id)
        }
        setListener()
        observe()
    }

    private fun observe() {
        with(viewModel) {
            videoCategory.observe(viewLifecycleOwner) { list ->
                list?.apply {
                    videoCategoryList = list.toMutableList()
                    updateInfo()
                    binding.tvVideoCategory.setOnClickListener { showCategories(it, this) }
                }
            }
            videoLevel.observe(viewLifecycleOwner) { list ->
                list?.apply {
                    videoLevelList = list.toMutableList()
                    updateInfo()
                    binding.tvVideoLevel.setOnClickListener { showLevels(it, this) }
                }
            }
            closed.observe(viewLifecycleOwner) {
                if (it == true) {
                    onBack()
                }
            }
            update.observe(viewLifecycleOwner) {
                with(binding) {
                    if (it) {
                        etUrlVideo.setText(videoItem.url)
                        etVideoTitle.setText(videoItem.title)
                        etVideoDescription.setText(videoItem.description)

                        updateInfo()

                        Glide.with(ivImg.context)
                            .load(videoItem.mainPhoto)
                            .error(R.drawable.ic_prew)
                            .into(ivImg)

                        isVideoValid()
                    }
                }
            }
        }
    }

    private fun updateInfo() {
        if (!id.isNullOrEmpty()) {

            videoCategoryList.forEach {
                if (it.id == viewModel.videoItem.categoryId) {
                    binding.tvVideoCategory.text = it.title
                }
            }
            videoLevelList.forEach {
                if (it.id == viewModel.videoItem.exerciseDifficultyLevelId) {
                    binding.tvVideoLevel.text = it.title
                }
            }
        }
    }

    private fun setListener() {
        with(binding) {
            ivBack.setOnClickListener {
                onBack()
            }
            btnSave.setOnClickListener {
                viewModel.createUserVideo()
            }
            tvCancel.setOnClickListener {
                viewModel.deleteUserVideo()
            }
            ivAddPhoto.setOnClickListener {
                setCoverImageDialog()
            }
            etUrlVideo.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.videoItem.url = s.toString()
                }
            })
            etVideoTitle.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.videoItem.title = s.toString()
                }
            })
            etVideoDescription.addTextChangedListener(object : EasyTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.videoItem.description = s.toString()
                }
            })
        }
    }

    private fun isVideoValid() {
        with(viewModel.videoItem) {
            binding.btnSave.enableIf(
                !url.isNullOrEmpty() &&
                        !title.isNullOrEmpty() &&
                        !description.isNullOrEmpty() &&
                        !categoryId.isNullOrEmpty() &&
                        !exerciseDifficultyLevelId.isNullOrEmpty()
            )
        }
    }

    private fun showCategories(view: View, videoList: List<VideoSelector>) {
        val popupMenu = PopupMenu(requireContext(), view)

        for (video in videoList) {
            popupMenu.menu.add(0, videoList.indexOf(video), 0, video.title)
        }

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            binding.tvVideoCategory.text = videoList[item.itemId].title
            viewModel.videoItem.categoryId = videoList[item.itemId].id
            isVideoValid()
            true
        }

        popupMenu.show()
    }

    private fun showLevels(view: View, videoList: List<VideoSelector>) {
        val popupMenu = PopupMenu(requireContext(), view)

        for (video in videoList) {
            popupMenu.menu.add(0, videoList.indexOf(video), 0, video.title)
        }

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            binding.tvVideoLevel.text = videoList[item.itemId].title
            viewModel.videoItem.exerciseDifficultyLevelId = videoList[item.itemId].id
            isVideoValid()
            true
        }

        popupMenu.show()
    }

    private fun onBack() {
        requireActivity().onBackPressed()
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