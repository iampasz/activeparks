package com.app.activeparks.ui.event.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.app.activeparks.data.model.points.RoutePoint
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.ui.dialog.BottomSearchDialog
import com.app.activeparks.ui.event.interfaces.ResponseCallBack
import com.app.activeparks.ui.event.util.EventController
import com.app.activeparks.ui.event.util.EventHelper
import com.app.activeparks.ui.event.util.EventTypes
import com.app.activeparks.ui.event.viewmodel.EventRouteViewModel
import com.app.activeparks.util.ChangeDateType
import com.app.activeparks.util.MapsViewController
import com.app.activeparks.util.cropper.CropImage
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.removeFragment
import com.app.activeparks.util.extention.replaceFragment
import com.app.activeparks.util.extention.visible
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentEventCreateBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class FragmentEventCreate : Fragment() {


    private val requestCode = 22

    lateinit var binding: FragmentEventCreateBinding
    private lateinit var mapsViewController: MapsViewController
    private val viewModel: EventRouteViewModel by sharedViewModel()
    private var eventData = ItemEvent()
    var currentTrainingType = ""
    var geoPointsList = ArrayList<GeoPoint>()
    var markerList = ArrayList<Marker>()
    var markerType = 0
    lateinit var eventController: EventController
    private lateinit var currentPhotoPath: String
    private var photoURI: Uri? = null
    private var formattedDateTime = ""
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
                    eventController.loadFileToAPI(file, eventData)
                    binding.imageCover.setImageURI(resultUri)

                    Log.i("CHECK_IMAGE", "Image was loaded")
                    Log.i("CHECK_IMAGE", "${checkFieldsAndEnableButton()} result")
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventCreateBinding
            .inflate(inflater, container, false)

        MapsViewController(binding.eventMap, requireContext())
        initSpinner()
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventController = EventController(requireContext())
        mapsViewController = MapsViewController(binding.eventMap, requireContext())

        var currentPoint = 0
        val myListener: Marker.OnMarkerDragListener = object : Marker.OnMarkerDragListener {
            override fun onMarkerDrag(marker: Marker) {
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onMarkerDragEnd(marker: Marker) {
                geoPointsList[currentPoint].longitude = marker.position.longitude
                geoPointsList[currentPoint].latitude = marker.position.latitude
                EventHelper.drawRoute(geoPointsList, binding.eventMap)
                EventHelper.drawMarkers(binding.eventMap, geoPointsList, this, markerType)
                getAddress(marker.position)
            }

            override fun onMarkerDragStart(marker: Marker) {
                for ((i, position) in geoPointsList.withIndex()) {
                    if (position == marker.position) {
                        currentPoint = i
                    }
                }
            }
        }

        observer(myListener)
        //TODO save scroll to view model
        // val previousScrollPosition = viewModel.getScrollPosition()
        // binding.scroll.post { binding.scroll.scrollTo(0, previousScrollPosition) }

        binding.scroll.viewTreeObserver.addOnScrollChangedListener {
            //TODO save scroll to view model
            // val currentScrollPosition = binding.scroll.scrollY
            // viewModel.saveScrollPosition(currentScrollPosition)
        }

        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        formattedDateTime = currentDateTime.format(formatter)

        with(binding) {

            imageCover.setOnClickListener { setCoverImageDialog() }
            backButton.setOnClickListener {
                alertBeforeClosing()
            }
            startData.setOnClickListener { setDate(startData) }
            endData.setOnClickListener { setDate(endData) }
            startData.text = formattedDateTime
            endData.text = formattedDateTime
            openFullMap.setOnClickListener { openFullMap() }
            eventMap.setOnTouchListener { _, _ ->
                scroll.requestDisallowInterceptTouchEvent(true)
                false
            }
            eventMap.overlays.add(MapEventsOverlay(object : MapEventsReceiver {
                @RequiresApi(Build.VERSION_CODES.TIRAMISU)
                override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                    when (currentTrainingType) {
                        EventTypes.SIMPLE_TRAINING.type -> {
                            geoPointsList.clear()
                        }
                    }
                    p?.let {
                        geoPointsList.add(it)
                        markerList.clear()
                        EventHelper.drawRoute(geoPointsList, binding.eventMap)
                        EventHelper.drawMarkers(
                            binding.eventMap,
                            geoPointsList,
                            myListener,
                            markerType
                        )
                        getAddress(it)
                    }

                    return true
                }

                override fun longPressHelper(p: GeoPoint?): Boolean {
                    return false
                }


            }))

            val publishResponseSuccessful = object : ResponseCallBack {
                override fun load(responseFromApi: String) {
                    viewModelStore.clear()
                    parentFragmentManager.removeFragment(this@FragmentEventCreate)
                }

            }
            val setDataResponseSuccessful = object : ResponseCallBack {
                override fun load(responseFromApi: String) {
                    viewModelStore.clear()
                    eventController.publishDataEvent(eventData.id, publishResponseSuccessful)
                    parentFragmentManager.removeFragment(FragmentEventCreate())

                }
            }

            buttonPublish.setOnClickListener { publishFielldEvent(setDataResponseSuccessful) }
            editFullDescription.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                @SuppressLint("SetTextI18n")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val charCount = s?.length ?: 0
                    val maxTextSize = 255
                    binding.textCounter.text = "$charCount / $maxTextSize"
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })

            checkEditTextListener(editNameEvent)
            checkEditTextListener(editDescriptionEvent)
            checkEditTextListener(editFullDescription)

            editAdress.setOnClickListener { setAddressBySearch(myListener) }
        }
    }

    private fun fetchData(
        context: Context,
        coordinate: GeoPoint
    ): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var address = ""
        try {

            @Suppress("DEPRECATION") val addresses = geocoder.getFromLocation(
                coordinate.latitude,
                coordinate.longitude,
                1
            )
            if (addresses != null) {
                address = addresses[0].getAddressLine(0) ?: ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return address
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getAddress(p: GeoPoint) {

        val geocoder = Geocoder(requireContext())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            GlobalScope.launch(Dispatchers.Default) {
                val listener = Geocoder.GeocodeListener { result ->
                    val address = result[0]
                    val addressLines = address.getAddressLine(0)
                    binding.editAdress.setText(addressLines)
                }
                geocoder.getFromLocation(p.latitude, p.longitude, 1, listener)
            }
        } else {
            val addressLines = fetchData(requireContext(), p)
            binding.editAdress.setText(addressLines)
        }
    }

    private fun choseTime(year: Int, month: Int, day: Int, chosenData: TextView) {
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                chosenData.text =
                    ChangeDateType.formatDateTime(year, month, day, hourOfDay, minute)
                compareStartEndDate()
            },
            LocalTime.now().hour,
            LocalTime.now().minute,
            true
        )
        timePickerDialog.show()
    }

    private fun initSpinner() {
        val data = arrayOf(
            getString(R.string.simple),
            getString(R.string.with_route),
            getString(R.string.online_training)
        )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                binding.eventMap.visible()
                binding.openFullMap.gone()

                when (position) {
                    0 -> {
                        binding.openFullMap.visible()
                        currentTrainingType = EventTypes.SIMPLE_TRAINING.type
                        markerType = 0
                    }

                    1 -> {
                        binding.openFullMap.visible()
                        currentTrainingType = EventTypes.ROUTE_TRAINING.type
                        markerType = 1
                    }

                    2 -> {
                        binding.eventMap.gone()
                        currentTrainingType = EventTypes.ONLINE_TRAINING.type
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

    }

    private fun collectEventData() {
        with(eventData) {
            title = binding.editNameEvent.text.toString()
            fullDescription = binding.editFullDescription.text.toString()
            shortDescription = binding.editDescriptionEvent.text.toString()
            typeId = currentTrainingType
            routePoints = getRoutePointFromGeoPointList()
        }
    }

    private fun getRoutePointFromGeoPointList(): List<RoutePoint> {
        val routePointList = ArrayList<RoutePoint>()
        for ((i, geoPoint) in geoPointsList.withIndex()) {

            val routePoint = RoutePoint()
            routePoint.location = listOf(geoPoint.latitude, geoPoint.longitude)
            routePoint.pointIndex = i


            when (eventData.typeId) {
                EventTypes.SIMPLE_TRAINING.type -> routePoint.type = 0
                EventTypes.ROUTE_TRAINING.type -> routePoint.type = 1
                EventTypes.ONLINE_TRAINING.type -> routePoint.type = 2
            }

            routePointList.add(routePoint)
        }
        return routePointList
    }

    private fun alertBeforeClosing() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(requireActivity().resources.getString(R.string.event_not_publish))
        builder.setMessage(requireActivity().resources.getString(R.string.save_druft))
        builder.setPositiveButton(requireActivity().resources.getString(R.string.yes)) { _, _ ->
            collectEventData()
            val responseSuccessful = object : ResponseCallBack {
                override fun load(responseFromApi: String) {
                    viewModelStore.clear()
                    parentFragmentManager.removeFragment(this@FragmentEventCreate)
                }
            }
            eventController.setDataEvent(responseSuccessful, eventData)
        }
        builder.setNegativeButton(requireActivity().resources.getString(R.string.no)) { _, _ ->
            eventData.id?.let {
                eventController.deleteEvent(eventData.id)
                viewModelStore.clear()
            }
            parentFragmentManager.removeFragment(this)
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun observer(myListener: Marker.OnMarkerDragListener) {
        val lastPoint = viewModel.getLastMapGeoPoint()
        binding.eventMap.controller.setCenter(lastPoint)

        viewModel.dataEvent.observe(viewLifecycleOwner) { newData ->

            eventData = newData
            with(binding) {

                editNameEvent.setText(newData.title)
                editFullDescription.setText(newData.fullDescription)
                editDescriptionEvent.setText(newData.shortDescription)
                startData.text =
                    ChangeDateType.formatDateTime(newData.startsAt)
                endData.text =
                    ChangeDateType.formatDateTime(newData.finishesAt)
            }

            currentTrainingType = newData.typeId ?: EventTypes.SIMPLE_TRAINING.type

            when (currentTrainingType) {
                EventTypes.SIMPLE_TRAINING.type -> {
                    binding.spinner.setSelection(0)
                    markerType = 0
                }

                EventTypes.ROUTE_TRAINING.type -> {
                    binding.spinner.setSelection(1)
                    markerType = 1
                }

            }
            newData?.imageUrl?.let { Picasso.get().load(it).into(binding.imageCover) }
        }

        viewModel.getGeoPointsLiveData().observe(viewLifecycleOwner) { geoPoints ->
            geoPoints?.let {

                if (geoPoints.size > 0) {
                    EventHelper.drawRoute(it, binding.eventMap)
                    EventHelper.drawMarkers(binding.eventMap, it, myListener, markerType)
                    geoPointsList = it
                }
            }
        }
    }

    private fun setDate(editText: TextView) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                choseTime(year, month, day, editText)
            },


            LocalDate.now().year,
            LocalDate.now().monthValue - 1,
            LocalDate.now().dayOfMonth
        )

        datePickerDialog.show()
    }

    private fun checkFieldsAndEnableButton() {
        val isField1Filled = binding.editNameEvent.text.isNotBlank()
        val isField2Filled = binding.editDescriptionEvent.text.isNotBlank()
        val isField3Filled = binding.editFullDescription.text.isNotBlank()
        val isAllFieldsFilled = isField1Filled && isField2Filled && isField3Filled

        binding.imageCover.drawable?.let {
            if (isAllFieldsFilled) {
                binding.buttonPublish.text = "Опублікувати"
            } else {
                binding.buttonPublish.text = "Зберегти зміни"
            }
        }
    }

    private fun checkEditTextListener(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkFieldsAndEnableButton()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun setCoverImageDialog() {
        val dialog = BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme)
        dialog.setContentView(R.layout.dialog_gallery)
        val galleryAction = dialog.findViewById<LinearLayout>(R.id.gallery_action)
        galleryAction?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                getContentLauncher.launch("image/*")
            } else {
                Log.i("KNJNJNF", "${ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)}  hh")
                Toast.makeText(
                    activity,
                    getString(R.string.add_access_to_photo),
                    Toast.LENGTH_SHORT
                ).show()


                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), requestCode)

//                ActivityCompat.requestPermissions(
//                    requireActivity(),
//                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                    2
//                )

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
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
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

                Activity.RESULT_CANCELED -> {
                }

                else -> {
                }
            }
        }


    private fun setAddressBySearch(myListener: Marker.OnMarkerDragListener) {
        val addPhotoBottomDialogFragment =
            BottomSearchDialog.newInstance().setOnCliclListener { lat, lon ->
                mapsViewController.setPositionMap(lat, lon)
                geoPointsList.clear()


                val myPoint = GeoPoint(lat, lon)

                geoPointsList.add(myPoint)
                markerList.clear()
                EventHelper.drawRoute(geoPointsList, binding.eventMap)
                EventHelper.drawMarkers(
                    binding.eventMap,
                    geoPointsList,
                    myListener,
                    markerType
                )
                getAddress(myPoint)

                //  EventHelper.drawMarkers()
                //viewModel.setUpdateSportsGroundList(50, lat, lon)
            }
        addPhotoBottomDialogFragment.show(
            requireActivity().supportFragmentManager,
            "fragment_search"
        )
    }


    private fun compareStartEndDate() {

        val myCompareStart =
            compareStartDates(
                binding.startData.text.toString(),
                ChangeDateType.formatDateTime(formattedDateTime).toString()
            )


        if (myCompareStart) {
            val myCompare =
                compareDates(binding.startData.text.toString(), binding.endData.text.toString())

            if (!myCompare) {
                Toast.makeText(
                    context,
                    getString(R.string.date_later_end),
                    Toast.LENGTH_SHORT
                ).show()
                binding.endData.text = binding.startData.text
            }
        } else {
            Toast.makeText(
                context,
                getString(R.string.date_before_current),
                Toast.LENGTH_SHORT
            ).show()

            binding.startData.text = ChangeDateType.formatDateTime(formattedDateTime).toString()
        }
    }

    private fun compareDates(startDateText: String, endDateText: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val startDate = LocalDateTime.parse(startDateText, formatter)
        val endDate = LocalDateTime.parse(endDateText, formatter)
        return !endDate.isBefore(startDate)
    }

    private fun compareStartDates(startDateText: String, currentDateText: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

        val startDate = LocalDateTime.parse(startDateText, formatter)
        val endDate = LocalDateTime.parse(currentDateText, formatter)

        return !startDate.isBefore(endDate)
    }

    private fun openFullMap() {
        collectEventData()
        viewModel.setGeoPoints(geoPointsList)
        //viewModel.updateItemEventData(eventData)
        viewModel.setLastMapGeoPoint(binding.eventMap.mapCenter)
        parentFragmentManager.replaceFragment(
            R.id.constrain_events_container,
            FragmentChangeRoute()
        )
    }

    private fun publishFielldEvent(setDataResponseSuccessful: ResponseCallBack) {
        collectEventData()
        if (EventHelper.checkFields(binding)) {

            if (eventData.imageUrl != null) {
                eventController.setDataEvent(setDataResponseSuccessful, eventData)
            } else {
                Toast.makeText(context, "Додайте зображення", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Не всі поля заповнені", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            this.requestCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Дозвіл надано, виконайте ваші дії зчитування зовнішнього сховища тут
                } else {
                    // Дозвіл не надано, ви можете повідомити користувача або спробувати запросити знову
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}

