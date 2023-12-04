package com.app.activeparks.ui.event.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.app.activeparks.data.model.points.RoutePoint
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.ui.event.interfaces.responseSuccessful
import com.app.activeparks.ui.event.util.EventController
import com.app.activeparks.ui.event.util.EventHelper
import com.app.activeparks.ui.event.viewmodel.EventRouteViewModel
import com.app.activeparks.util.ChangeDateType
import com.app.activeparks.util.MapsViewController
import com.app.activeparks.util.cropper.CropImage
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.onlineTraining
import com.app.activeparks.util.extention.removeFragment
import com.app.activeparks.util.extention.replaceFragment
import com.app.activeparks.util.extention.routeTraining
import com.app.activeparks.util.extention.simpleTraining
import com.app.activeparks.util.extention.visible
import com.squareup.picasso.Picasso
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentEventCreateBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class FragmentEventCreate : Fragment() {

    lateinit var binding: FragmentEventCreateBinding
    private val viewModel: EventRouteViewModel by activityViewModels()
    private var eventData = ItemEvent()
    var currentTrainingType = ""
    var geoPointsList = ArrayList<GeoPoint>()
    var markerList = ArrayList<Marker>()
    var markerType = 0
    lateinit var eventController: EventController

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

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventController = EventController(requireContext())

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


        val previousScrollPosition = viewModel.getScrollPosition()
        binding.scroll.post { binding.scroll.scrollTo(0, previousScrollPosition) }

        binding.scroll.viewTreeObserver.addOnScrollChangedListener {
            val currentScrollPosition = binding.scroll.scrollY
            viewModel.saveScrollPosition(currentScrollPosition)
        }

        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)


        with(binding) {

            imageCover.setOnClickListener { getContentLauncher.launch("image/*") }
            backButton.setOnClickListener {
                alertBeforeClosing()
            }
            startData.setOnClickListener { setDate(startData) }
            endData.setOnClickListener { setDate(endData) }
            startData.text = (formattedDateTime)
            endData.text = (formattedDateTime)

            binding.openFullMap.setOnClickListener {
                collectEventData()
                viewModel.setGeoPoints(geoPointsList)
                viewModel.setLastMapGeoPoint(binding.eventMap.mapCenter)
                parentFragmentManager.replaceFragment(
                    R.id.frame_events_container,
                    FragmentChangeRoute()
                )

            }

            eventMap.setOnTouchListener { _, _ ->
                scroll.requestDisallowInterceptTouchEvent(true)
                false
            }

            eventMap.overlays.add(MapEventsOverlay(object : MapEventsReceiver {
                @RequiresApi(Build.VERSION_CODES.TIRAMISU)
                override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {

                    when (currentTrainingType) {
                        simpleTraining() -> {
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

            val publishResponseSuccessful = object : responseSuccessful {
                override fun load() {
                    parentFragmentManager.removeFragment(this@FragmentEventCreate)
                }
            }

            val setDataResponseSuccessful = object : responseSuccessful {
                override fun load() {

                    eventController.publishDataEvent(eventData.id, publishResponseSuccessful)
                    parentFragmentManager.removeFragment(FragmentEventCreate())

                }
            }

            buttonPublish.setOnClickListener {
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
        }

        binding.editFullDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val charCount = s?.length ?: 0

                binding.textCounter.text = "$charCount / 255"
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })


        checkEditTextListener(binding.editNameEvent)
        checkEditTextListener(binding.editDescriptionEvent)
        checkEditTextListener(binding.editFullDescription)


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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun choseTime(year: Int, month: Int, day: Int, textView: TextView) {
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                //textView.text = formatDateTime(year, month, day, hourOfDay, minute)
                textView.text = ChangeDateType.formatDateTime(year, month, day, hourOfDay, minute)
            },
            LocalTime.now().hour,
            LocalTime.now().minute,
            false
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
                        currentTrainingType = simpleTraining()
                        markerType = 0
                    }

                    1 -> {
                        binding.openFullMap.visible()
                        currentTrainingType = routeTraining()
                        markerType = 1
                    }

                    2 -> {
                        binding.eventMap.gone()
                        currentTrainingType = onlineTraining()
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
            startsAt = ChangeDateType.formatDateTimeReverse(binding.startData.text.toString())
            finishesAt = ChangeDateType.formatDateTimeReverse(binding.endData.text.toString())
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
                simpleTraining() -> routePoint.type = 0
                routeTraining() -> routePoint.type = 1
                onlineTraining() -> routePoint.type = 2
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

            val responseSuccessful = object : responseSuccessful {
                override fun load() {
                    viewModelStore.clear()
                    parentFragmentManager.removeFragment(this@FragmentEventCreate)
                }
            }
            eventController.setDataEvent(responseSuccessful, eventData)
        }

        builder.setNegativeButton(requireActivity().resources.getString(R.string.no)) { _, _ ->
            eventData.id?.let {
                eventController.deleteEvent(eventData.id)
            }

            viewModelStore.clear()
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


            currentTrainingType = newData.typeId ?: simpleTraining()


            when (currentTrainingType) {
                simpleTraining() -> {
                    binding.spinner.setSelection(0)
                    markerType = 0
                }

                routeTraining() -> {
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDate(editText: TextView) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                choseTime(year, month, day, editText)
            },
            LocalDate.now().year,
            LocalDate.now().monthValue,
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

    override fun onDestroy() {
        super.onDestroy()
        eventController.clearDisposables()
    }
}

