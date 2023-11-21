package com.app.activeparks.ui.event

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.activeparks.data.model.Default
import com.app.activeparks.data.model.points.RoutePoint
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.data.network.ApiService
import com.app.activeparks.data.network.NetworkModule
import com.app.activeparks.data.repository.Repository
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.util.MapsViewController
import com.app.activeparks.util.cropper.CropImage
import com.google.gson.Gson
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentEventCreateBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Collections
import java.util.Locale


class FragmentEventCreate : Fragment() {

    lateinit var binding: FragmentEventCreateBinding

    private var currentImage = ""
    var typeOfEvent = 0
    var trainingType = "848e3121-4a2b-413d-8a8f-ebdd4ecf2840"

    private lateinit var startPoint: GeoPoint
    lateinit var routePoints: ArrayList<GeoPoint>
    lateinit var repository: Repository
    lateinit var preferences: Preferences
    private lateinit var apiService: ApiService
    private var centerMarker: Marker? = null

    private val getContentLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            val cropIntent = CropImage.activity(uri)
                .setAspectRatio(3, 2)
                .getIntent(requireContext())

            cropActivityResultLauncher.launch(cropIntent)

        }

    private val cropActivityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val resultUri = CropImage.getActivityResult(data).uri
                    //val croppedImageUri = result.uri
                    val file = saveImageToFile(resultUri)
                    loadFileToAPI(file)

                    binding.imageCover.setImageURI(resultUri)
                }
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventCreateBinding
            .inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val networkModule = NetworkModule()

        MapsViewController(binding.eventMap, requireContext())

        routePoints = ArrayList()
        apiService = networkModule.test
        preferences = Preferences(requireContext())
        preferences.server = true
        repository = Repository(preferences)
//


        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)

        binding.startData.text = (formattedDateTime)
        binding.endData.text = (formattedDateTime)

        //  binding.removePoint.setOnClickListener { clickForRoute() }

        binding.addPoint.setOnClickListener {
            if (typeOfEvent == 0) addPoint()
            if (typeOfEvent == 1) clickForRoute()

        }

        binding.imageCover.setOnClickListener { galleryDialog() }
        binding.backButton.setOnClickListener { closeFragment() }
        binding.startData.setOnClickListener { setDate(binding.startData) }
        binding.endData.setOnClickListener { setDate(binding.endData) }

        binding.eventMap.setOnTouchListener { _, _ ->
            binding.scroll.requestDisallowInterceptTouchEvent(true)
            false
        }

        initSpinner()

        binding.eventMap.overlays.add(MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                p?.let { routePoints.add(it) }
                // drawRoute(routePoints)
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }
        }))

        binding.buttonPublish.setOnClickListener { loadDataToAPI() }

    }

    private fun clickForRoute() {
        startPoint = binding.eventMap.mapCenter as GeoPoint
        routePoints.add(startPoint)
        drawRoute(routePoints)
    }

    private fun drawRoute(points: ArrayList<GeoPoint>) {

        binding.eventMap.overlayManager.removeAll(Collections.singleton(Polyline()))

        val road = Road(points)

        val roadOverlay = RoadManager.buildRoadOverlay(
            road,
            Color.BLUE,
            2f
        )
        binding.eventMap.overlays.add(roadOverlay)

    }

    private fun addPoint() {
        val centerCoordinates = binding.eventMap.mapCenter as GeoPoint

        val address = GeocodingAsyncTask().fetchData(requireContext(), centerCoordinates)
        binding.editAdress.setText(address)

        setMarker(centerCoordinates)
    }

    private fun setMarker(geoPoint: GeoPoint) {
        if (centerMarker != null) {
            binding.eventMap.overlayManager.remove(centerMarker)
        }

        val icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_pin_green)

        centerMarker = Marker(binding.eventMap)
        centerMarker?.icon = icon
        centerMarker?.position = geoPoint
        centerMarker?.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)


        binding.eventMap.overlayManager.add(centerMarker)
        binding.eventMap.invalidate()
    }

    private fun galleryDialog() {
        getContentLauncher.launch("image/*")
    }

    private fun saveImageToFile(imageUri: Uri): File {
        val resolver = activity?.contentResolver
        val file: File?

        val inputStream = resolver?.openInputStream(imageUri)

        file = File(requireActivity().filesDir, "image.jpg")

        val outputStream = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var bytesRead: Int

        while (inputStream?.read(buffer).also { bytesRead = it!! } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }

        inputStream?.close()


        return file
    }

    private fun choseTime(year: Int, month: Int, day: Int, textView: TextView) {
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                textView.text = formatDateTime(year, month, day, hourOfDay, minute)
            },
            5,
            20,
            false
        )

        timePickerDialog.show()
    }

    private fun formatDateTime(year: Int, month: Int, day: Int, hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(calendar.time)
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

                binding.eventMap.visibility = View.VISIBLE
                binding.removePoint.visibility = View.GONE
                binding.addPoint.visibility = View.GONE

                typeOfEvent = position

                when (typeOfEvent) {
                    0 -> {
                        trainingType = "848e3121-4a2b-413d-8a8f-ebdd4ecf2840"
                        binding.addPoint.visibility = View.VISIBLE
                    }

                    1 -> {
                        trainingType = "bd09f36f-835c-49e4-88b8-4f835c1602ac"
                        binding.addPoint.visibility = View.VISIBLE
                        binding.removePoint.visibility = View.VISIBLE
                    }

                    2 -> {
                        trainingType = "e58e5c86-5ca7-412f-94f0-88effd1a45a8"
                        binding.eventMap.visibility = View.GONE
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    @SuppressLint("CheckResult")
    private fun loadFileToAPI(file: File) {
        repository.updateFile(file, "other_photo")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result: Default ->
                    if (result.url != null) {
                        currentImage = result.url
                    }
                }
            ) {
            }
    }

    @SuppressLint("CheckResult")
    fun loadDataToAPI() {
        if (checkFields()) {
            repository.createEmptyEvent()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe { responseBody ->
                    val jsonString = responseBody.string()
                    val gson = Gson()
                    val eventData = gson.fromJson(jsonString, ItemEvent::class.java)
                    val eventId = eventData.id

                    setDataEvent(eventId, getUserEventData())
                }
        } else {
            Toast.makeText(context, "Fell up all fields", Toast.LENGTH_SHORT).show()
        }

    }

    @SuppressLint("CheckResult")
    private fun setDataEvent(eventId: String, itemEvent: ItemEvent) {
        repository.setDataEvent(eventId, itemEvent).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ publishDataEvent(eventId) }) { }
    }

    @SuppressLint("CheckResult")
    private fun publishDataEvent(eventToken: String) {
        repository.publishDataEvent(eventToken)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ closeFragment() }
            ) { throwable -> Log.i("API SERVICE", throwable.message + " NOU") }
    }

    private fun getUserEventData(): ItemEvent {
        val itemEvent = ItemEvent()
        itemEvent.title = binding.editNameEvent.text.toString()
        itemEvent.fullDescription = binding.editFullDescription.text.toString()
        itemEvent.createdAt = binding.startData.text.toString()
        itemEvent.startsAt = binding.startData.text.toString()
        itemEvent.finishesAt = binding.endData.text.toString()
        itemEvent.shortDescription = binding.editDescriptionEvent.text.toString()

        itemEvent.typeId = trainingType

        val myPoints = ArrayList<RoutePoint>()

        routePoints[0]

        val routePoint1 = RoutePoint()
        routePoint1.type = 0
        val coordinates = listOf(50.123, 30.456)
        routePoint1.location = coordinates
        routePoint1.pointIndex = 0

        myPoints.add(routePoint1)

        itemEvent.routePoints = myPoints

        itemEvent.imageUrl = currentImage
        return itemEvent
    }

    private fun closeFragment() {
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    private fun checkFields(): Boolean {

        if (binding.editNameEvent.text.toString().trim().isEmpty()) {
            binding.scroll.smoothScrollTo(binding.editNameEvent.top, binding.editNameEvent.top)
            return false
        }

        if (binding.editFullDescription.text.toString().trim().isEmpty()) {
            binding.scroll.smoothScrollTo(
                binding.editFullDescription.top,
                binding.editFullDescription.top
            )
            return false
        }

        if (binding.editDescriptionEvent.text.toString().trim().isEmpty()) {
            binding.scroll.smoothScrollTo(
                binding.editDescriptionEvent.top,
                binding.editFullDescription.top
            )
            return false
        }

        return true
    }

    private fun setDate(editText: TextView) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                choseTime(year, month, day, editText)
            },
            2023,
            11,
            16
        )
        datePickerDialog.show()
    }

}