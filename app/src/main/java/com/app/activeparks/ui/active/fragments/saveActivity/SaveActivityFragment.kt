package com.app.activeparks.ui.active.fragments.saveActivity

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
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.app.activeparks.ui.active.ActiveViewModel
import com.app.activeparks.ui.active.fragments.level.ActivityInfoTrainingAdapter
import com.app.activeparks.ui.active.model.Feeling
import com.app.activeparks.ui.userProfile.model.PhotoType
import com.app.activeparks.util.EasyTextWatcher
import com.app.activeparks.util.MapsViewController
import com.app.activeparks.util.cropper.CropImage
import com.app.activeparks.util.extention.FileHelper
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.invisible
import com.app.activeparks.util.extention.replaceNull
import com.app.activeparks.util.extention.visibleIf
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentSaveActivityBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.views.overlay.Polyline
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SaveActivityFragment : Fragment() {

    private lateinit var binding: FragmentSaveActivityBinding

    private val viewModel: SaveActivityViewModel by viewModel()
    private val activityViewModel: ActiveViewModel by activityViewModels()
    private var count = 0
    private val maxCount = 255
    private var mapsViewController: MapsViewController? = null
    private var polyLine = Polyline()


    private var photoURI: Uri? = null
    private var currentPhotoPath = ""

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
                        binding.icActivity.setImageURI(resultUri)
                        viewModel.currentActivity.file = FileHelper.uriToFile(resultUri, requireContext())
                    }
                }
            }
        }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveActivityBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            setStartValue()
            setListener()
        }

        observe()
    }

    private fun observe() {
        with(viewModel) {
            saved.observe(viewLifecycleOwner) {
                if (it == true) {
                    onBack()
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun FragmentSaveActivityBinding.setStartValue() {
        viewModel.currentActivity.dateTime = SimpleDateFormat("dd MMM yyyy, HH:mm").format(Date())
        Feeling.getFiling().first().apply {
            viewModel.currentActivity.feeling = this
            tvFellTitle.text = this.title
            ivFeelIcon.setImageResource(this.img)
        }

        tvData.text = viewModel.currentActivity.dateTime

        viewModel.startInfo.startPoint.unit =
            activityViewModel.activityState.startPoint.replaceNull()
        viewModel.startInfo.weather.unit =
            activityViewModel.activityState.weather

        activityViewModel.activityState.apply {
            ivLocation.visibleIf(activityType.isOutside && startPoint.isNotEmpty() && activeRoad.isNotEmpty())
            ivWeather.visibleIf(activityType.isOutside && startPoint.isNotEmpty() && activeRoad.isNotEmpty() && activityType.isOutside && weather.isNotEmpty())

        }

        ivLocation.setActivityInfoItem(viewModel.startInfo.startPoint)
        calculateWidthLocation()
        ivWeather.setActivityInfoItem(viewModel.startInfo.weather)
        if (activityViewModel.activityState.weather.isNotEmpty()) {
            ivWeather.setTitle(activityViewModel.activityState.weather)
            ivWeather.setImg(FileHelper.getWeatherIconResource(activityViewModel.activityState.weatherIcon))
        } else {
            ivWeather.invisible()
        }

        viewModel.currentActivity.location = ivLocation.getItem()
        viewModel.currentActivity.weather = ivWeather.getItem()


        rvInfoView.apply {
            val adapterInfoItem = ActivityInfoTrainingAdapter {}
            adapter = adapterInfoItem
            layoutManager = GridLayoutManager(requireContext(), 2)

            val list = activityViewModel.activityState.activityInfoItems.filter {
                (!activityViewModel.activityState.activityType.isInclude || !it.isOutside) &&
                        (activityViewModel.activityState.isPulseGadgetConnected || !it.isPulseGadget)
            }

            adapterInfoItem.list.submitList(list)
        }

        if (activityViewModel.activityState.activityType.isInclude) {
            cardView.gone()

            val layoutParams = rvInfoView.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.topToBottom = R.id.tvFeelDescription
            rvInfoView.layoutParams = layoutParams
        } else {
            setCurrentLocation()
        }
        initFeelingAdapter()
    }

    private fun initFeelingAdapter() {
        val feelings = Feeling.getFiling()
        val popupMenu = PopupMenu(requireContext(), binding.vFeel)
        binding.tvFellTitle.setOnClickListener {
            popupMenu.show()
        }

        feelings.forEach { feeling ->
            popupMenu.menu.add(feeling.title).setIcon(feeling.img).setOnMenuItemClickListener {
                onFeelingSelected(feeling)
                true
            }
        }
    }

    private fun onFeelingSelected(feeling: Feeling) {
        with(binding) {
            tvFellTitle.text = feeling.title
            ivFeelIcon.setImageResource(feeling.img)
        }
    }

    @Suppress("DEPRECATION")
    private fun calculateWidthLocation() {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels


        if (binding.ivLocation.calculateDesiredWidth() > 0.5 * screenWidth) {
            val point = viewModel.startInfo.startPoint
            point.unit = point.unit.replace(", ", ",\n")
            binding.ivLocation.setActivityInfoItem(point)
        }
    }

    private fun FragmentSaveActivityBinding.setListener() {
        etDescriptionActivity.addTextChangedListener(object : EasyTextWatcher() {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                count = s?.toString()?.length ?: 0
                binding.tvCount.text = "$count/$maxCount"
                viewModel.currentActivity.descriptionActivity = s?.toString() ?: ""
            }
        })
        etNameTraining.addTextChangedListener(object : EasyTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                viewModel.currentActivity.titleActivity = s?.toString() ?: ""
            }
        })

        ivClose.setOnClickListener { onBack() }
        tvDeleteTraining.setOnClickListener { onBack() }

        btnSave.setOnClickListener {
            viewModel.saveActivity(
                activityViewModel.activityState,
                activityViewModel.activityState.activityInfoItems,
                activityViewModel.activityState.activityTime
            )

            binding.mapview.overlayManager.clear()
            binding.mapview.invalidate()
        }

        addImgActivity.setOnClickListener {
            setCoverImageDialog()
        }
    }

    private fun onBack() {
        requireActivity().onBackPressed()
    }

    private fun setCurrentLocation() {
        activityViewModel.activityState.activeRoad.takeIf { it.isNotEmpty() }?.apply {
            mapsViewController = MapsViewController(
                binding.mapview,
                context,
                activityViewModel.activityState.activeRoad.first()
            )
            mapsViewController?.homeView = true

            activityViewModel.apply {
                activityState.activeRoad.forEach {
                    polyLine.addPoint(it)
                }

                binding.mapview.overlayManager.add(polyLine)
                binding.mapview.invalidate()
            }
        } ?: kotlin.run {
            mapsViewController = MapsViewController(
                binding.mapview,
                context
            )
            mapsViewController?.homeView = true
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
}