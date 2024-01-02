package com.app.activeparks.ui.userProfile.video

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.app.activeparks.ui.userProfile.model.VideoCategory
import com.app.activeparks.ui.userProfile.model.VideoLevel
import com.technodreams.activeparks.databinding.FragmentUserProfileAddVideoBinding

/**
 * Created by O.Dziuba on 25.12.2023.
 */
class AddVideoUserProfile : Fragment() {

    private lateinit var binding: FragmentUserProfileAddVideoBinding
    private val imageActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImage: Intent? = result.data
            handleSelectedImage(selectedImage)
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

        setListener()
    }

    private fun setListener() {
        with(binding) {
            ivBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
            btnSave.setOnClickListener {
                requireActivity().onBackPressed()
            }
            tvCancel.setOnClickListener {
                requireActivity().onBackPressed()
            }
            ivAddVideo.setOnClickListener {
                openGalleryForImage()
            }
        }

        initVideCategory()
        initVideLevel()
    }


    private fun initVideCategory() {
        val categories = VideoCategory.getCategory()
        val popupMenu = PopupMenu(requireContext(), binding.vVideoCategory)
        binding.tvVideoCategory.setOnClickListener {
            popupMenu.show()
        }

        categories.forEach { item ->
            popupMenu.menu.add(item.title).setOnMenuItemClickListener {
                onCategorySelected(item)
                true
            }
        }
    }

    private fun onCategorySelected(videoCategory: VideoCategory) {
        with(binding) {
            tvVideoCategory.text = videoCategory.title
        }
    }

    private fun initVideLevel() {
        val levels = VideoLevel.geLevels()
        val popupMenu = PopupMenu(requireContext(), binding.vVideoLevel)
        binding.tvVideoLevel.setOnClickListener {
            popupMenu.show()
        }

        levels.forEach { level ->
            popupMenu.menu.add(level.title).setOnMenuItemClickListener {
                onLevelSelected(level)
                true
            }
        }
    }

    private fun handleSelectedImage(data: Intent?) {
        val selectedImage: Uri? = data?.data
        binding.ivImg.setImageURI(selectedImage)
    }

    private fun openGalleryForImage() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imageActivityResultLauncher.launch(galleryIntent)
    }

    private fun onLevelSelected(videoLevel: VideoLevel) {
        with(binding) {
            tvVideoLevel.text = videoLevel.title
        }
    }
}