package com.app.activeparks.ui.event

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Geocoder
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
import com.app.activeparks.data.model.points.RoutePoint
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.data.network.ApiService
import com.app.activeparks.data.network.NetworkModule
import com.app.activeparks.data.repository.Repository
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.ui.event.interfaces.LoadEventData
import com.app.activeparks.ui.event.viewmodel.EventRouteViewModel
import com.app.activeparks.util.ChangeDateType
import com.app.activeparks.util.MapsViewController
import com.app.activeparks.util.cropper.CropImage
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.visible
import com.squareup.picasso.Picasso
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentEventCreateBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale


class FragmentEventCreate : Fragment() {

    lateinit var binding: FragmentEventCreateBinding
    private val viewModel: EventRouteViewModel by activityViewModels()

    val simpleTraining = "848e3121-4a2b-413d-8a8f-ebdd4ecf2840"
    val routeTraining = "bd09f36f-835c-49e4-88b8-4f835c1602ac"
    val onlineTraining = "e58e5c86-5ca7-412f-94f0-88effd1a45a8"

    var currentTrainingType = ""
    var geoPointsList = ArrayList<GeoPoint>()
    var markerList = ArrayList<Marker>()

    private var eventData = ItemEvent()

    private val ratioWidth = 3
    private val ratioHeight = 2

    lateinit var repository: Repository
    lateinit var preferences: Preferences
    private lateinit var apiService: ApiService


    private val getContentLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
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
                    val file = saveImageToFile(resultUri)
                    loadFileToAPI(file)

                    binding.imageCover.setImageURI(resultUri)
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


        val lastPoint = viewModel.getLastMapGeoPoint()
        binding.eventMap.controller.setCenter(lastPoint)

        viewModel.dataEvent.observe(viewLifecycleOwner) { newData ->
            eventData = newData
            binding.editNameEvent.setText(newData.title)
            binding.editFullDescription.setText(newData.fullDescription)
            binding.editDescriptionEvent.setText(newData.shortDescription)
            binding.startData.text = ChangeDateType.formatDateTime(newData.startsAt)
            binding.endData.text = ChangeDateType.formatDateTime(newData.finishesAt)

            currentTrainingType = newData.typeId ?: simpleTraining

            when (currentTrainingType) {
                simpleTraining -> {
                    binding.spinner.setSelection(0)
                }

                routeTraining -> {
                    binding.spinner.setSelection(1)
                }

            }

            newData?.imageUrl?.let { Picasso.get().load(it).into(binding.imageCover) }

        }

        viewModel.getGeoPointsLiveData().observe(viewLifecycleOwner) { geoPoints ->
            geoPoints?.let {

                if (geoPoints.size > 0) {
                    drawRoute(it)
                    drawMarkers(it)
                    geoPointsList = it
                }
            }
        }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val previousScrollPosition = viewModel.getScrollPosition()
        binding.scroll.post { binding.scroll.scrollTo(0, previousScrollPosition) }

        binding.scroll.viewTreeObserver.addOnScrollChangedListener {
            val currentScrollPosition = binding.scroll.scrollY
            viewModel.saveScrollPosition(currentScrollPosition)
        }


        val networkModule = NetworkModule()
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)

        apiService = networkModule.test
        preferences = Preferences(requireContext())
        preferences.server = true
        repository = Repository(preferences)

        with(binding) {

            imageCover.setOnClickListener { galleryDialog() }
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
                openFragment()
            }

            eventMap.setOnTouchListener { _, _ ->
                scroll.requestDisallowInterceptTouchEvent(true)
                false
            }

            eventMap.overlays.add(MapEventsOverlay(object : MapEventsReceiver {
                @RequiresApi(Build.VERSION_CODES.TIRAMISU)
                override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {


                    when (currentTrainingType) {
                        simpleTraining -> {
                            geoPointsList.clear()
                        }
                    }

                    p?.let {

                        geoPointsList.add(it)
                        markerList.clear()
                        drawRoute(geoPointsList)
                        drawMarkers(geoPointsList)

                        addPoint(it)

                    }




                    return true
                }

                override fun longPressHelper(p: GeoPoint?): Boolean {
                    return false
                }


            }))


            buttonPublish.setOnClickListener {
                collectEventData()

                val loadEventData = object : LoadEventData {
                    override fun load() {
                        publishDataEvent(eventData.id)

                    }
                }

                setDataEvent(loadEventData)
            }
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
    private fun addPoint(p: GeoPoint) {

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
                        currentTrainingType = simpleTraining
                    }

                    1 -> {
                        binding.openFullMap.visible()
                        currentTrainingType = routeTraining
                    }

                    2 -> {
                        binding.eventMap.gone()
                        currentTrainingType = onlineTraining
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
                        eventData.imageUrl = result.url
                    }
                }
            ) {
            }
    }

    @SuppressLint("CheckResult")
    private fun setDataEvent(loaded: LoadEventData) {
        repository.setDataEvent(eventData).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loaded.load()
            }) { throwable ->
                Log.e("ERROR", "Error: ${throwable.message}")
            }
    }

    @SuppressLint("CheckResult")
    private fun publishDataEvent(eventToken: String) {

        if (checkFields()) {
            repository.publishDataEvent(eventToken)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ closeFragment() }
                ) { throwable -> Log.i("API SERVICE", throwable.message + " NOU") }
        } else {
            Toast.makeText(context, "Не всі поля заповнені", Toast.LENGTH_SHORT).show()
        }

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

    private fun openFragment() {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.frame_events_container, FragmentChangeRoute())
            .commit()
    }

    private fun drawRoute(points: ArrayList<GeoPoint>) {

        binding.eventMap.overlays.removeAll { it is Polyline }

        val road = Road(points)
        val roadOverlay = RoadManager.buildRoadOverlay(
            road,
            Color.BLUE,
            20f
        )
        binding.eventMap.overlays.add(roadOverlay)
    }

    private fun drawMarkers(points: ArrayList<GeoPoint>) {

        binding.eventMap.overlays.removeAll { it is Marker }
        markerList.clear()

        for ((index, p) in points.withIndex()) {
            val marker = Marker(binding.eventMap)
            marker.position = p

            when (currentTrainingType) {
                simpleTraining -> marker.icon =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_pin_green)

                routeTraining -> marker.icon = getMarkerByPosition(index)

            }

            marker.isDraggable = true
            marker.position
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)

            marker.setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
                override fun onMarkerDrag(marker: Marker) {
                }

                override fun onMarkerDragEnd(marker: Marker) {

                    for ((i, point) in markerList.withIndex()) {
                        if (point == marker) {
                            geoPointsList[i] = marker.position
                            point.position = marker.position
                            addPoint(marker.position)
                        }

                    }

                    drawRoute(geoPointsList)
                    drawMarkers(geoPointsList)

                }

                override fun onMarkerDragStart(marker: Marker) {

                }
            })

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
        val textSizeCircle = 40f
        val canvas = Canvas(bitmap)
        val x = (bitmap.width - paint.measureText(text)) / 2
        val y = (bitmap.height + paint.textSize) / 2

        paint.textSize = textSizeCircle
        paint.color = resources.getColor(R.color.white, null)
        canvas.drawText(text, x, y, paint)

        return bitmap
    }

    private fun combineBitmaps(background: Bitmap, overlay: Bitmap): Bitmap {
        val combined = Bitmap.createBitmap(background.width, background.height, background.config)
        val canvas = Canvas(combined)
        val left = 0f
        val top = 0f

        canvas.drawBitmap(background, left, top, null)
        canvas.drawBitmap(overlay, left, top, null)

        return combined
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
                simpleTraining -> routePoint.type = 0
                routeTraining -> routePoint.type = 1
                onlineTraining -> routePoint.type = 2
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

            val loadEventData = object : LoadEventData {
                override fun load() {
                    closeFragment()
                }
            }
            setDataEvent(loadEventData)
        }

        builder.setNegativeButton(requireActivity().resources.getString(R.string.no)) { _, _ ->
            eventData.id?.let {
                deleteEvent(eventData.id)
            }
            closeFragment()
        }

        val dialog = builder.create()
        dialog.show()
    }

    @SuppressLint("CheckResult")
    fun deleteEvent(eventId: String) {

        repository.deleteEvent(eventId)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.i("API_SERVICE", "Data was delete from API")
            }
            ) { Log.i("API_SERVICE", "Data wasn't delete from API") }
    }


}

