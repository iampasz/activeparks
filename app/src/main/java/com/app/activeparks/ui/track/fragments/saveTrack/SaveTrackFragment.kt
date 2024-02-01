package com.app.activeparks.ui.track.fragments.saveTrack

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
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
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.app.activeparks.MainActivity
import com.app.activeparks.data.model.track.PointsTrack
import com.app.activeparks.ui.routeActive.fragments.saveRouteActive.SaveRouteActiveFragment
import com.app.activeparks.ui.track.fragments.changeMapTrack.DataCallback
import com.app.activeparks.ui.track.fragments.changeMapTrack.TrackChangeMapFragment
import com.app.activeparks.ui.track.model.Complexity
import com.app.activeparks.ui.track.model.RoadSurface
import com.app.activeparks.ui.track.model.TrackTypes
import com.app.activeparks.ui.userProfile.activityInfo.gallery.ActivityInfoGalleryAdapter
import com.app.activeparks.ui.userProfile.activityInfo.gallery.ImageGalleryFragment
import com.app.activeparks.util.AddressOfDoubleUtil
import com.app.activeparks.util.EasyTextWatcher
import com.app.activeparks.util.MapsViewController
import com.app.activeparks.util.cropper.CropImage
import com.app.activeparks.util.extention.FileHelper
import com.app.activeparks.util.extention.toast
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentSaveTrackBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class SaveTrackFragment : Fragment(), DataCallback {

    private lateinit var binding: FragmentSaveTrackBinding

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
                        if (viewModel.isGallery == false) {
                            binding.ivMainPhoto.setImageURI(resultUri)
                        }
                        FileHelper.uriToFile(resultUri, requireContext())?.let { viewModel.saveImage(it) }
                    }
                }
            }
        }

    private val viewModel: SaveTrackViewModel by viewModel()
    private var adapter: ActivityInfoGalleryAdapter? = null
    private var count = 0
    private val maxCount = 255
    private var mapsViewController: MapsViewController? = null
    private var polyLine = Polyline()

    private var photoURI: Uri? = null
    private var currentPhotoPath = ""

    companion object {
        fun show(trackId: String): SaveTrackFragment {
            val fragment = SaveTrackFragment()
            val args = Bundle()
            args.putString("track_id", trackId)
            fragment.arguments = args
            return fragment
        }

        fun insert(routeActive: List<GeoPoint>): SaveTrackFragment {
            val fragment = SaveTrackFragment()
            val args = Bundle()
            args.putString("insert_track", Gson().toJson(routeActive))
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveTrackBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            parseIntent()
            setListener()
            initView()
            observe()
        }
    }

    private fun parseIntent() {
        arguments?.getString("insert_track")?.let {
            viewModel.activityRoad.addAll(Gson().fromJson(it, Array<GeoPoint>::class.java).toList())
            viewModel.insert()
        }
        arguments?.getString("track_id")?.let {
            viewModel.getTrack(it)
        }

    }

    private fun FragmentSaveTrackBinding.setListener() {

        ivBack.setOnClickListener { requireActivity().onBackPressed() }

        etDescriptionTrack.addTextChangedListener(object : EasyTextWatcher() {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                count = s?.toString()?.length ?: 0
                binding.tvCount.text = "$count/$maxCount"
                viewModel.trackDate?.value?.description = s?.toString() ?: ""
            }
        })

        etNameTrack.addTextChangedListener(object : EasyTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                viewModel.trackDate?.value?.name = s?.toString() ?: ""
            }
        })


        etAdress.addTextChangedListener(object : EasyTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                viewModel.trackDate?.value?.address = s?.toString() ?: ""
            }
        })

        tvDeleteTrack.setOnClickListener {
            viewModel.removeTrack()
        }

        bSaveTrack.setOnClickListener {
            viewModel.saveTrack()
            requireActivity().onBackPressed()
        }

        bIntegrity.setOnClickListener {
            selectIntegrity()
        }

        tvEditMap.setOnClickListener {
            viewModel.trackDate.value?.pointsTrack.let {
                val fragment = TrackChangeMapFragment()
                val bundle = Bundle().apply {
                    putParcelableArrayList("pointsList", ArrayList(it))
                }
                fragment.arguments = bundle
                fragment.setDataCallback(this@SaveTrackFragment)
                openFragment(fragment)
            }
        }

        tvTime.setOnClickListener {
            showTimePickerDialog()
        }

        bMainPhoto.setOnClickListener {
            viewModel.isGallery = false
            setCoverImageDialog()
        }

        bPublicTrack.setOnClickListener {
            viewModel.trackDate.value?.id?.let {
                openFragment(SaveRouteActiveFragment.insert(it))
                requireActivity().onBackPressed()
            }
        }

        bAddGallery.setOnClickListener {
            viewModel.trackDate.value?.photos?.size?.let {
                if (it < 30) {
                    viewModel.isGallery = true
                    setCoverImageDialog()
                } else {
                    toast(requireContext(), getString(R.string.tv_add_max_photo_count))
                }
            } ?: setCoverImageDialog()
        }

        initComplexityAdapter()
        initRoadSurfaceAdapter()
        initTrackTypes()
    }

    private fun observe() {
        with(viewModel) {
            trackDate.observe(viewLifecycleOwner) { response ->
                response.dateStartRecord.let {
                    binding.tvStratAdress.setText(it)
                }

                response.pointsTrack?.takeIf { it.isNotEmpty() }?.apply {
                    response.pointsTrack?.last()?.let {
                        val address =
                            AddressOfDoubleUtil.getAddressFromLocation(
                                requireContext(), it.latitude,
                                it.longitude
                            )
                        binding.tvFinishAdress.text = address
                    }
                }

                response.name.let {
                    binding.etNameTrack.setText(it)
                }

                response.description.let {
                    binding.etDescriptionTrack.setText(it)
                }

                response.address.let {
                    binding.tvStratAdress.setText(it)
                }

                response.recommendedTime.let {
                    binding.tvTime.setText(it)
                }

                response.routeLength.let {
                    binding.tvDistance.setText(it + " км")
                }

                response.type.let {typeId ->
                    TrackTypes.getTrackTypes().find { it.id == typeId  }?.let {
                        with(binding) {
                            tvActivityType.text = it!!.title
                            tvActivityType.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.white
                                )
                            )
                            ivActivityType.setImageResource(it!!.img)
                            viewModel.trackDate.value?.type = it!!.id
                        }
                    }
                }

                response.complexityId.let {complexityId ->
                    Complexity.getComplexity().find { it.id == complexityId  }?.let {
                    with(binding) {
                        tvComplexity.text = it!!.title
                        tvComplexity.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                        ivComplexity.setImageResource(it!!.img)
                        ivComplexity.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
                        vComplexity.background = resources.getDrawable(it!!.background, null)
                        viewModel.trackDate.value?.complexityId = it!!.id
                    }
                    }
                }

                response.coverType.let {coverTypeId ->
                    if (coverTypeId.isNotEmpty()) {
                        RoadSurface.getRoadSurface().find { it.id == coverTypeId?.get(0) }.let {
                            binding.tvRoadSurface.text = it?.title
                        }
                    }
                }

                response.coverImage.let {
                    Glide.with(requireContext()).load(it).error(R.drawable.ic_prew)
                        .into(binding.ivMainPhoto)
                }

                response.integrity.let {
                    if (it == true){
                        binding.bIntegrity.background = resources.getDrawable(R.drawable.background_green,null)
                        binding.bIntegrity.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    }else{
                        binding.bIntegrity.background = resources.getDrawable(R.drawable.button_gray_video,null)
                        binding.bIntegrity.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
                    }
                }

                response.pointsTrack?.let {
                    setCurrentLocation(it)
                }

                response.photos?.let {
                    adapter?.list?.submitList(photos)
                }
            }
            back.observe(viewLifecycleOwner) {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun initView() {
        with(binding) {
            adapter = ActivityInfoGalleryAdapter(resources) {
                openFragment(ImageGalleryFragment(it))
            }
            rvGallery.adapter = adapter
            rvGallery.layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    private fun initComplexityAdapter() {
        val popupMenu = PopupMenu(requireContext(), binding.vComplexity)
        binding.tvComplexity.setOnClickListener {
            popupMenu.show()
        }

        Complexity.getComplexity().forEach { complexity ->
            popupMenu.menu.add(complexity.title).setIcon(complexity.img).setOnMenuItemClickListener {
                onComplexitySelected(complexity)
                true
            }
        }
    }

    private fun onComplexitySelected(complexity: Complexity) {
        with(binding) {
            tvComplexity.text = complexity.title
            tvComplexity.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            ivComplexity.setImageResource(complexity.img)
            ivComplexity.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
            vComplexity.background = resources.getDrawable(complexity.background,null)
            viewModel.trackDate.value?.complexityId = complexity.id
        }
    }

    private fun initRoadSurfaceAdapter() {
        val popupMenu = PopupMenu(requireContext(), binding.vComplexity)
        binding.tvRoadSurface.setOnClickListener {
            popupMenu.show()
        }

        RoadSurface.getRoadSurface().forEach { item ->
            popupMenu.menu.add(item.title).setOnMenuItemClickListener {
                with(binding) {
                    tvRoadSurface.text = item.title
                    viewModel.trackDate.value?.coverType = mutableListOf(item.id)
                }
                true
            }
        }
    }

    private fun initTrackTypes() {
        val popupMenu = PopupMenu(requireContext(), binding.tvActivityType)
        binding.tvActivityType.setOnClickListener {
            popupMenu.show()
        }

        TrackTypes.getTrackTypes().forEach { item ->
            popupMenu.menu.add(item.title).setOnMenuItemClickListener {
                with(binding) {
                    tvActivityType.text = item.title
                    viewModel.trackDate.value?.type = item.id
                }
                true
            }
        }
    }

    private fun setCurrentLocation(points: List<PointsTrack>) {
        points.takeIf { it.isNotEmpty() }?.apply {
            points[0].let {
                mapsViewController?.setPositionMap(it.latitude, it.longitude)
                mapsViewController = MapsViewController(
                    binding.mapview,
                    context,
                    GeoPoint(it.latitude, it.longitude)
                )
                mapsViewController?.homeView = true
                points.forEach {
                    polyLine.addPoint(GeoPoint(it.latitude, it.longitude))
                }
                binding.mapview.overlayManager.add(polyLine)
                binding.mapview.invalidate()
            }
        }
    }


    private fun selectIntegrity() {
        if (viewModel.trackDate.value?.integrity == true){
            binding.bIntegrity.background = resources.getDrawable(R.drawable.background_green,null)
            binding.bIntegrity.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            viewModel.trackDate.value?.integrity = false
        }else{
            binding.bIntegrity.background = resources.getDrawable(R.drawable.button_gray_video,null)
            binding.bIntegrity.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color))
            viewModel.trackDate.value?.integrity = true
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                binding.tvTime.text = formattedTime
                viewModel.trackDate.value?.recommendedTime = formattedTime
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
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

    private fun openFragment(fragment: Fragment) =
        (requireActivity() as? MainActivity)?.addFragment(fragment)

    override fun onDataReceived(data: List<PointsTrack>) {
        viewModel.trackDate.value?.pointsTrack = data
    }

}