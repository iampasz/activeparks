package com.app.activeparks.ui.event

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ClipData.Item
import android.content.Intent
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import com.google.android.gms.common.util.IOUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.gson.Gson
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentEventCreateBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

    var currentImage = "";
    var eventName = 0;
    var typeOfEvent = 0;
    var trainingType = "848e3121-4a2b-413d-8a8f-ebdd4ecf2840"
    var startDate = "";
    var endDate = "";
    val firstCoordinate = listOf(50.123, 30.456)



    lateinit var startPoint: GeoPoint;
    lateinit var routePoints: ArrayList<GeoPoint>;
    lateinit var binding: FragmentEventCreateBinding

    lateinit var repository: Repository;
    lateinit var preferences: Preferences;
    lateinit var apiService: ApiService;
    lateinit var mFusedLocationClient: FusedLocationProviderClient;
    var centerMarker: Marker? = null;

    val getContentLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            CropImage.activity(uri).setAspectRatio(3, 2)
                .start(requireContext(), this)

            val file = uri?.let { saveImageToFile(it) }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventCreateBinding
            .inflate(inflater, container, false);

        return binding.root;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var mapsViewControler =
            MapsViewController(binding.eventMap, getContext());
        var networkModule = NetworkModule();


        routePoints = ArrayList();
        apiService = networkModule.getTest();
        preferences = Preferences(getContext());
        preferences.setServer(true);
        repository = Repository(preferences);

        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)

        binding.startData.setText(formattedDateTime)
        binding.endData.setText(formattedDateTime)

      //  binding.removePoint.setOnClickListener { clickForRoute() }

        binding.addPoint.setOnClickListener {
            if (typeOfEvent == 0)  addPoint()
            if (typeOfEvent == 1) clickForRoute()

        }

        binding.mainCover.setOnClickListener { galleryDialog() }

        binding.backButton.setOnClickListener { closeFragment() }

        binding.startData.setOnClickListener {

            var datePickerDialog = DatePickerDialog(
                requireContext(),
                { DatePicker, year, month, day ->
                    choseTime(year, month, day, binding.startData)
                },
                2023,
                11,
                16
            );
            datePickerDialog.show();
        }

        binding.endData.setOnClickListener {

            var datePickerDialog = DatePickerDialog(
                requireContext(),
                { DatePicker, year, month, day ->
                    choseTime(year, month, day, binding.endData)
                },
                2023,
                11,
                16
            );
            datePickerDialog.show();
        }

        binding.eventMap.setOnTouchListener { _, event ->
            // Потребляем событие касания, чтобы остановить его передачу в NestedScrollView
            binding.scroll.requestDisallowInterceptTouchEvent(true)
            false
        }

        setSpiner()


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

    fun clickForRoute() {
        startPoint = binding.eventMap.getMapCenter() as GeoPoint;
        routePoints.add(startPoint)
        drawRoute(routePoints);
    }

    fun drawRoute(points: List<GeoPoint>) {

        binding.eventMap.overlayManager.removeAll(Collections.singleton(Polyline()))
        var route = Polyline();
        route.setPoints(points);
        route.setColor(R.color.black); // Синий цвет
        binding.eventMap.getOverlayManager().add(route);
        binding.eventMap.invalidate(); // Перерисовка карты

    }

    fun addPoint() {
        val centerCoordinates = binding.eventMap.getMapCenter() as GeoPoint;

        val geocodingTask = GeocodingAsyncTask(requireContext(), centerCoordinates) { result ->
            binding.editAdress.setText(result)
        }

        geocodingTask.execute()

        setMarker(centerCoordinates)
    }

    fun setMarker(geoPoint: GeoPoint) {
        // Очистка предыдущего маркера, если он существует
        if (centerMarker != null) {
            binding.eventMap.getOverlayManager().remove(centerMarker);
        }

        // Создание маркера
        var icon = getResources().getDrawable(R.drawable.ic_pin_green);

        centerMarker = Marker(binding.eventMap);
        centerMarker?.setIcon(icon);
        centerMarker?.setPosition(geoPoint);
        centerMarker?.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

        // Добавление маркера на карту
        binding.eventMap.getOverlayManager().add(centerMarker);
        binding.eventMap.invalidate(); // Перерисовка карты
    }

    fun galleryDialog() {
        getContentLauncher.launch("image/*")
    }

    fun saveImageToFile(imageUri: Uri): File {
        val resolver = getActivity()?.getContentResolver();
        var file: File? = null;


        val inputStream = resolver?.openInputStream(imageUri);

        file = File(requireActivity().filesDir, "image.jpg")

        val outputStream = FileOutputStream(file);
        val buffer = ByteArray(1024)
        var bytesRead: Int;

        while (inputStream?.read(buffer).also { bytesRead = it!! } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }

        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);


        return file;
    }

    fun choseTime(year: Int, month: Int, day: Int, textView: TextView) {
        val timePickerDialog = TimePickerDialog(
            getContext(),
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                textView.setText(formatDateTime(year, month, day, hourOfDay, minute));
            },
            5,
            20,
            false
        );

        timePickerDialog.show();
    }

    fun formatDateTime(year: Int, month: Int, day: Int, hour: Int, minute: Int): String {
        var calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                // Отримайте URI обрізаного зображення
                Log.i("CROPIMM", "WE ARE зображення")
                val croppedImageUri = result.uri
                val file = saveImageToFile(croppedImageUri)
                loadFileToAPI(file, "other_photo")

                binding.imageCover.setImageURI(croppedImageUri)
                // Використовуйте croppedImageUri за потребою
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                // Обробка помилок обрізання зображення
                val error = result.error
                Log.i("CROPIMM", error.toString() + "WE ARE зображення")
            }
        }
    }


    private fun setSpiner() {
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

                binding.mapContainer.visibility = View.VISIBLE
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
                        binding.mapContainer.visibility = View.GONE
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }


    @SuppressLint("CheckResult")
    private fun loadFileToAPI(file: File, type: String) {
        repository.updateFile(file, type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result: Default ->
                    if (result.url != null) {
                        Log.i("DOSOMEDOING", result.url + " ggg" + result.error)
                        currentImage = result.url;
                    }
                }
            ) { error: Throwable ->
                Log.i(
                    "DOSOMEDOING",
                    "Перевірте підключення до інтернету " + error.message
                )
            }
    }

    @SuppressLint("CheckResult")
    fun loadDataToAPI() {
        if(chekFields()){
            repository.createEmptyEvent()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe { responseBody ->
                    val jsonString = responseBody.string()
                    val gson = Gson()
                    val eventData = gson.fromJson(jsonString, ItemEvent::class.java)
                    val eventId = eventData.id

                    setDataEvent(eventId, getUserEventData())
                }
        }else{
            Toast.makeText(context, "Не всі поля заповенні", Toast.LENGTH_SHORT).show()
        }

    }

    @SuppressLint("CheckResult")
    private fun setDataEvent(eventId: String, itemEvent: ItemEvent) {
        repository.setDataEvent(eventId, itemEvent).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ publishDataEvent(eventId) }) { throwable -> }
    }

    @SuppressLint("CheckResult")
    private fun publishDataEvent(eventToken: String) {
        repository.publishDataEvent(eventToken)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ closeFragment() }
            ) { throwable -> Log.i("APISERVICE", throwable.message + " NOU") }
    }

    fun getUserEventData(): ItemEvent {
        val itemEvent = ItemEvent()
        itemEvent.title = binding.editNameEvent.text.toString()
        itemEvent.fullDescription = binding.editFullDescription.text.toString()
        itemEvent.createdAt = binding.startData.text.toString();
        itemEvent.startsAt = binding.startData.text.toString();
        itemEvent.finishesAt = binding.endData.text.toString();
        itemEvent.shortDescription = binding.editDescriptionEvent.text.toString()

        itemEvent.typeId = trainingType

        var myPoints = ArrayList<RoutePoint>();

        routePoints.get(0)

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


    fun closeFragment() {
        parentFragmentManager.beginTransaction().remove(this).commit();
    }

    fun chekFields():Boolean{

        if(binding.editNameEvent.text.toString().trim().isEmpty()){
            binding.scroll.smoothScrollTo(binding.editNameEvent.top, binding.editNameEvent.top)
            return false
        }

        if(binding.editFullDescription.text.toString().trim().isEmpty()){
            binding.scroll.smoothScrollTo(binding.editFullDescription.top, binding.editFullDescription.top)
            return false
        }

        if(binding.editDescriptionEvent.text.toString().trim().isEmpty()){
            binding.scroll.smoothScrollTo(binding.editDescriptionEvent.top, binding.editFullDescription.top)
            return false
        }

        return true
    }

}