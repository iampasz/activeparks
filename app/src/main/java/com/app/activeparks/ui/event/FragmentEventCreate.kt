package com.app.activeparks.ui.event

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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
import androidx.fragment.app.activityViewModels
import com.app.activeparks.data.model.Default
import com.app.activeparks.data.model.events.CounterPointModel
import com.app.activeparks.data.model.points.RoutePoint
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.data.network.ApiService
import com.app.activeparks.util.GeocodingAsyncTask
import com.app.activeparks.data.network.NetworkModule
import com.app.activeparks.data.repository.Repository
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.ui.event.viewmodel.EventRouteViewModel
import com.app.activeparks.util.MapsViewController
import com.app.activeparks.util.cropper.CropImage
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.visible
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
import org.osmdroid.views.overlay.Marker.OnMarkerDragListener
import org.osmdroid.views.overlay.Polyline
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


class FragmentEventCreate : Fragment() {

    lateinit var binding: FragmentEventCreateBinding
    private val sharedViewModel: EventRouteViewModel by activityViewModels()

    private var currentImage = ""
    var typeOfEvent = 0
    var trainingType = "848e3121-4a2b-413d-8a8f-ebdd4ecf2840"

    val simpleTraining = "848e3121-4a2b-413d-8a8f-ebdd4ecf2840"
    val routeTraining = "bd09f36f-835c-49e4-88b8-4f835c1602ac"
    val onlineTraining = "e58e5c86-5ca7-412f-94f0-88effd1a45a8"

    var geoPointsList = ArrayList<GeoPoint>()
    var markerList = ArrayList<Marker>()

    private var currentYear = 0
    private var currentMonth = 0
    private var currentDay = 0
    private var currentHour = 0
    private var currentMinutes = 0
    private val widthRoutLine = 10f
    private val colorRouteLine = Color.RED
    private val textSizeCircle = 40f

    private lateinit var startPoint: GeoPoint
   // lateinit var routePoints: ArrayList<GeoPoint>

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

                data?.let {
                    val resultUri = CropImage.getActivityResult(data).uri
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

        observeGeoPoints()

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initSpinner()

        val networkModule = NetworkModule()
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)

        //routePoints = ArrayList()
        apiService = networkModule.test
        preferences = Preferences(requireContext())
        preferences.server = true
        repository = Repository(preferences)

        with(binding) {

            MapsViewController(eventMap, requireContext())

            imageCover.setOnClickListener { galleryDialog() }
            backButton.setOnClickListener {


                closeFragment()
            }
            startData.setOnClickListener { setDate(startData) }
            endData.setOnClickListener { setDate(endData) }
            startData.text = (formattedDateTime)
            endData.text = (formattedDateTime)

            addPoint.setOnClickListener {

                openFragment()
                when (typeOfEvent) {
                    0 -> addPoint()
                  //  1 -> clickForRoute()
                }
            }



            eventMap.overlays.add(MapEventsOverlay(object : MapEventsReceiver {
                override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
//                    p?.let {
//                        routePoints.add(it)
//                        addMarkerCurrent(it)
//                    }

                    p?.let {

                        geoPointsList.add(it)
                        markerList.clear()
                        drawRoute(geoPointsList)
                        drawMarkers(geoPointsList)


                    }

                    return true
                }

                override fun longPressHelper(p: GeoPoint?): Boolean {
                    return false
                }
            }))


            buttonPublish.setOnClickListener { loadDataToAPI() }

        }
    }

//    private fun clickForRoute() {
//        startPoint = binding.eventMap.mapCenter as GeoPoint
//        routePoints.add(startPoint)
//        //drawRoute(counterPointList)
//    }

    private fun addPoint() {
        val centerCoordinates = binding.eventMap.mapCenter as GeoPoint

        val address = GeocodingAsyncTask().fetchData(requireContext(), centerCoordinates)
        binding.editAdress.setText(address)

        setMarker(centerCoordinates)
    }

    private fun setMarker(geoPoint: GeoPoint) {

        centerMarker?.let {
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
            currentHour,
            currentMinutes,
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

                binding.eventMap.visible()
                binding.removePoint.gone()
                binding.addPoint.gone()

                typeOfEvent = position

                when (typeOfEvent) {
                    0 -> {
                        trainingType = simpleTraining
                        binding.addPoint.visible()
                    }

                    1 -> {
                        trainingType = routeTraining
                        binding.addPoint.visible()
                        binding.removePoint.visible()
                    }

                    2 -> {
                        trainingType = onlineTraining
                        binding.eventMap.gone()
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
                    result.url?.let {
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
            Toast.makeText(context, "Не всі поля заповнені", Toast.LENGTH_SHORT).show()
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

       // routePoints[0]

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

        with(binding) {

            if (editNameEvent.text.toString().trim().isEmpty()) {
                scroll.smoothScrollTo(editNameEvent.top, editNameEvent.top)
                return false
            }


            if (editFullDescription.text.toString().trim().isEmpty()) {
                scroll.smoothScrollTo(
                    editFullDescription.top,
                    editFullDescription.top
                )
                return false
            }

            if (editDescriptionEvent.text.toString().trim().isEmpty()) {
                scroll.smoothScrollTo(
                    editDescriptionEvent.top,
                    editDescriptionEvent.top
                )
                return false
            }
        }
        return true
    }

    private fun setDate(editText: TextView) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                choseTime(year, month, day, editText)
            },
            currentYear,
            currentMonth,
            currentDay

        )
        datePickerDialog.show()


    }

    private var markerCounter = 0


//    private fun drawTextToBitmap(bitmap: Bitmap, text: String): Bitmap {
//        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
//        paint.textSize = textSizeCircle
//        paint.color = resources.getColor(R.color.white, null)
//
//        val canvas = Canvas(bitmap)
//        val x = (bitmap.width - paint.measureText(text)) / 2
//        val y = (bitmap.height + paint.textSize) / 2
//
//        canvas.drawText(text, x, y, paint)
//
//        return bitmap
//    }
//
//    private fun combineBitmaps(background: Bitmap, overlay: Bitmap): Bitmap {
//        val combined = Bitmap.createBitmap(background.width, background.height, background.config)
//        val canvas = Canvas(combined)
//
//        canvas.drawBitmap(background, 0f, 0f, null)
//        canvas.drawBitmap(overlay, 0f, 0f, null)
//
//        return combined
//    }


    var myCounterPointer = ArrayList<CounterPointModel>()

//    private fun addMarkerCurrent(geoPoint: GeoPoint) {


    private fun sendDataToViewModel(geoPoints: ArrayList<GeoPoint>) {
        sharedViewModel.setGeoPoints(geoPoints)
    }

    private fun openFragment() {

        parentFragmentManager
            .beginTransaction()
            .add(R.id.frame_events_container, FragmentChangeRoute())
            .addToBackStack(null)
            .commit()

        sendDataToViewModel(geoPointsList)
    }


    private fun observeGeoPoints() {
        sharedViewModel.geoPointsLiveData.observe(viewLifecycleOwner) { geoPoints ->

            binding.eventMap.overlays.removeAll { it is Polyline }
            binding.eventMap.overlays.removeAll { it is Marker }

            geoPointsList = geoPoints
            if(geoPoints.size>0){
                drawRoute(geoPointsList)
                drawMarkers(geoPointsList)
            }

        }
    }


    private fun drawRoute(points: ArrayList<GeoPoint>) {

        binding.eventMap.overlays.removeAll { it is Polyline }

        val road = Road(points)
        val roadOverlay = RoadManager.buildRoadOverlay(
            road,
            colorRouteLine,
            widthRoutLine
        )
        binding.eventMap.overlays.add(roadOverlay)

    }

    private fun drawMarkers(points: ArrayList<GeoPoint>) {


        binding.eventMap.overlays.removeAll { it is Marker }
        markerList.clear()

        for ((index, p) in points.withIndex()) {
            val marker = Marker(binding.eventMap)
            marker.position = p
            marker.icon = getMarkerByPosition(index)
           // marker.isDraggable = true
            marker.position
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)

//            marker.setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
//                override fun onMarkerDrag(marker: Marker) {
//                }
//
//                override fun onMarkerDragEnd(marker: Marker) {
//
//                    for ((i, point) in markerList.withIndex()) {
//                        if (point == marker) {
//                            geoPointsList[i] = marker.position
//                            point.position = marker.position
//                        }
//                    }
//
//                    drawRoute(geoPointsList)
//                    drawMarkers(geoPointsList)
//
//                }
//
//                override fun onMarkerDragStart(marker: Marker) {
//
//                }
//            })

            markerList.add(marker)
            binding.eventMap.overlays.add(marker)

        }

        binding.eventMap.invalidate()
    }

    private fun getMarkerByPosition(pointNumber: Int): Drawable {
        val text = pointNumber.toString()
        val radius = 50f
        val centerX = 50f
        val centerY = 50f

        val colorCircle = Color.BLUE

        val bitmap =
            Bitmap.createBitmap((radius * 2).toInt(), (radius * 2).toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = colorCircle
        paint.isAntiAlias = true


        canvas.drawCircle(centerX, centerY, radius, paint)
        val textBitmap = drawTextToBitmap(bitmap, text)
        val combinedBitmap = combineBitmaps(bitmap, textBitmap)

        return BitmapDrawable(resources, combinedBitmap)
    }

    private fun drawTextToBitmap(bitmap: Bitmap, text: String): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = textSizeCircle
        paint.color = resources.getColor(R.color.white, null)

        val canvas = Canvas(bitmap)
        val x = (bitmap.width - paint.measureText(text)) / 2
        val y = (bitmap.height + paint.textSize) / 2

        canvas.drawText(text, x, y, paint)

        return bitmap
    }

    private fun combineBitmaps(background: Bitmap, overlay: Bitmap): Bitmap {
        val combined = Bitmap.createBitmap(background.width, background.height, background.config)
        val canvas = Canvas(combined)

        canvas.drawBitmap(background, 0f, 0f, null)
        canvas.drawBitmap(overlay, 0f, 0f, null)

        return combined
    }

}

