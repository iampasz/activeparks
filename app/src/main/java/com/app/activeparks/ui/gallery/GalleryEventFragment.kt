package com.app.activeparks.ui.gallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.app.activeparks.MainActivity
import com.app.activeparks.ui.event.fragments.MainEventViewModel
import com.app.activeparks.ui.event.util.EventHelper
import com.app.activeparks.ui.event.viewmodel.EventViewModel
import com.app.activeparks.ui.gallery.adapter.GalleryAdapter
import com.app.activeparks.util.cropper.CropImage
import com.app.activeparks.util.extention.ImageTypes
import com.app.activeparks.util.extention.mainAddFragment
import com.technodreams.activeparks.databinding.FragmentGalleryBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class GalleryEventFragment : Fragment() {

    lateinit var binding: FragmentGalleryBinding
    private val viewModel: MainEventViewModel by activityViewModel()
    private val eventModel: EventViewModel by activityViewModel()
    private val galleryViewModel: GalleryViewModel by activityViewModel()

    val adapter = GalleryAdapter {

        val imageGalleryFragment = ImageGalleryFragment()

        val bundle = Bundle()
        bundle.putInt("CURRENT_POSITION", it)
        imageGalleryFragment.arguments = bundle
        (requireActivity() as? MainActivity)?.let {
            mainAddFragment(requireActivity() as MainActivity, imageGalleryFragment)
        }
    }

    val adapterOfficial = GalleryAdapter {

        val imageGalleryFragment = ImageGalleryFragment()

        val bundle = Bundle()
        bundle.putInt("CURRENT_POSITION", it)
        imageGalleryFragment.arguments = bundle
        (requireActivity() as? MainActivity)?.let {
            mainAddFragment(requireActivity() as MainActivity, imageGalleryFragment)
        }
    }

    //private lateinit var currentPhotoPath: String

   // private var photoURI: Uri? = null

    private val getContentLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            val ratioWidth = 3
            val ratioHeight = 2
            val cropIntent = CropImage.activity(uri)
                .setAspectRatio(ratioWidth, ratioHeight)
                .getIntent(requireContext())
            cropActivityResultLauncher.launch(cropIntent)
        }

    private val cropActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                data?.let {
                    val resultUri = CropImage.getActivityResult(data).uri
                    val file = EventHelper.saveImageToFile(resultUri, requireActivity())
                    viewModel.uploadFile(file, ImageTypes.TYPE_SPORT_EVENT.type, eventModel.currentId.value)
                    binding.imageCover.setImageURI(resultUri)
                }
            }
        }

//    private val takePictureLauncherAgain =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            when (result.resultCode) {
//                Activity.RESULT_OK -> {
//                    val imageUri = photoURI
//                    val ratioWidth = 3
//                    val ratioHeight = 2
//                    val cropIntent = CropImage.activity(imageUri)
//                        .setAspectRatio(ratioWidth, ratioHeight)
//                        .getIntent(requireContext())
//                    cropActivityResultLauncher.launch(cropIntent)
//                }
//
//                Activity.RESULT_CANCELED -> {
//                }
//            }
//        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding
            .inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageCover.setOnClickListener {
            getContentLauncher.launch("image/*")
        }

        viewModel.getPhotoGalleryOfficial(eventModel.currentId.value.toString())
        viewModel.getPhotoGalleryUser(eventModel.currentId.value.toString())

        initView()
        obsereve()
    }

//    private fun setCoverImageDialog() {
//        val dialog = BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
//        dialog.setContentView(R.layout.dialog_gallery)
//        val galleryAction = dialog.findViewById<LinearLayout>(R.id.gallery_action)
//        val cameraAction = dialog.findViewById<LinearLayout>(R.id.camera_action)
//        val cancel = dialog.findViewById<LinearLayout>(R.id.cancel)
//
//        galleryAction?.setOnClickListener {
//            getContentLauncher.launch("image/*")
//            dialog.dismiss()
//        }
//        cameraAction?.setOnClickListener {
//            if (ContextCompat.checkSelfPermission(
//                    requireActivity(),
//                    Manifest.permission.CAMERA
//                )
//                == PackageManager.PERMISSION_GRANTED
//            ) {
//                dispatchTakePictureIntent()
//            } else {
//                Toast.makeText(
//                    activity,
//                    getString(R.string.add_access_to_camera),
//                    Toast.LENGTH_SHORT
//                )
//                    .show()
//                ActivityCompat.requestPermissions(
//                    requireActivity(),
//                    arrayOf(Manifest.permission.CAMERA),
//                    5
//                )
//            }
//            dialog.dismiss()
//        }
//        cancel?.setOnClickListener { dialog.dismiss() }
//        dialog.show()
//    }
//
//    private fun dispatchTakePictureIntent() {
//        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
//                val photoFile: File? = try {
//                    createImageFile()
//                } catch (ex: IOException) {
//                    null
//                }
//                photoFile?.also {
//                    val photoURI: Uri = FileProvider.getUriForFile(
//                        requireContext(),
//                        "com.example.android.fileprovider",
//                        it
//                    )
//
//                    this.photoURI = photoURI
//                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                    takePictureLauncherAgain.launch(takePictureIntent)
//                }
//            }
//        }
//    }

//    @SuppressLint("SimpleDateFormat")
//    @Throws(IOException::class)
//    private fun createImageFile(): File {
//        // Create an image file name
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val storageDir: File? =
//            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        return File.createTempFile(
//            "JPEG_${timeStamp}_", /* prefix */
//            ".jpg", /* suffix */
//            storageDir /* directory */
//        ).apply {
//            // Save a file: path for use with ACTION_VIEW intents
//            currentPhotoPath = absolutePath
//        }
//    }

    private fun obsereve() {
        viewModel.userPhotoUpdated.observe(viewLifecycleOwner) { list ->
            adapter.list.submitList(list.items)
            galleryViewModel.photoItemList.value = list.items

        }

        viewModel.officialPhotoUpdated.observe(viewLifecycleOwner){list ->
            adapterOfficial.list.submitList(list.items)
            galleryViewModel.photoOfficialItemList.value = list.items
        }


    }

    private fun initView() {
        val spanCount = 3
        val layoutManager = GridLayoutManager(requireContext(), spanCount)
        val layoutManagerOfficial = GridLayoutManager(requireContext(), spanCount)

        binding.rvGallery.layoutManager = layoutManager
        binding.rvGallery.adapter = adapter

        binding.rvGalleryOfficial.layoutManager = layoutManagerOfficial
        binding.rvGalleryOfficial.adapter = adapterOfficial
    }


}

