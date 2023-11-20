package com.app.activeparks.ui.event;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.activeparks.data.model.events.EventData;
import com.app.activeparks.data.model.events.UserEvent;
import com.app.activeparks.data.network.ApiService;
import com.app.activeparks.data.network.NetworkModule;
import com.app.activeparks.data.repository.Repository;
import com.app.activeparks.data.storage.Preferences;

import com.app.activeparks.util.MapsViewController;
import com.app.activeparks.util.cropper.CropImage;
import com.google.android.gms.common.util.IOUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.gson.Gson;
import com.technodreams.activeparks.R;
import com.technodreams.activeparks.databinding.FragmentEventCreateBinding;

import org.osmdroid.bonuspack.location.GeocoderNominatim;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;



import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


public class FragmentEventCreate extends Fragment {

    String imageLink;
    String eventName;
    String shortDescription;
    String fullDescription;
    String startDate;
    String endDate;
    int typeOfEvent = 0;

    private GeoPoint startPoint;
    private List<GeoPoint> routePoints;

    private FragmentEventCreateBinding binding;
    private Repository repository;
    private Preferences preferences;
    ApiService apiService;
    private FusedLocationProviderClient mFusedLocationClient;
    private Marker centerMarker;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentEventCreateBinding.inflate(inflater, container, false);
        MapsViewController mapsViewControler = new MapsViewController(binding.eventMap, getContext());
        routePoints = new ArrayList<>();
        NetworkModule networkModule = new NetworkModule();
        apiService = networkModule.getTest();
        preferences = new Preferences(getContext());
        preferences.setServer(true);
        repository = new Repository(preferences);


        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.removePoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickForRoute();
            }
        });

        //initMap();

        binding.addPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(typeOfEvent==1){
                    clickForRoute();
                }
                if(typeOfEvent==2){
                    addPoint();
                }
            }
        });


        binding.mainCover.setOnClickListener(click -> galleryDialog());
        // binding.buttonPublish.setOnClickListener(click -> checkForEmpty());

        binding.startData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        choseTime(year, month, day, binding.startData);

                    }
                }, 2023, 11, 16);
                datePickerDialog.show();
            }
        });

        binding.endData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        choseTime(year, month, day, binding.endData);

                    }
                }, 2023, 11, 16);
                datePickerDialog.show();
            }
        });


        setSpiner();


        binding.eventMap.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Потребляем событие касания, чтобы остановить его передачу в NestedScrollView
                binding.scroll.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

//        // Устанавливаем слушатель событий касания для NestedScrollView
//        binding.scroll.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // Разрешаем перехватывать событие касания NestedScrollView
//                binding.eventMap.requestDisallowInterceptTouchEvent(false);
//                return false;
//            }
//        });


        binding.eventMap.getOverlays().add(new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                // Добавление новой точки маршрута
                routePoints.add(p);

                // Построение маршрута
                drawRoute(routePoints);

                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        }));

    }

    void galleryDialog() {
        getContentLauncher.launch("image/*");

        new ActivityResultCallback<>() {
            @Override
            public void onActivityResult(Object o) {

            }
        };
    }


    private ActivityResultLauncher<String> getContentLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {

                CropImage.activity(uri).setAspectRatio(3, 2)
                        .start(requireContext(), this);


                Log.i("CROPIMM", "WE ARE HERE");


                // Uri croppedImageUri = uri;
                File file = saveImageToFile(uri);

                //loadFileToAPI(file,"other_photo");

            });


    @SuppressLint("CheckResult")
    private void loadFileToAPI(File file, String type) {
        repository.updateFile(file, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result.getUrl() != null) {
                                Log.i("DOSOMEDOING", result.getUrl() + " ggg" + result.getError());
                            }
                        },
                        error -> {
                            Log.i("DOSOMEDOING", "Перевірте підключення до інтернету " + error.getMessage());
                        }
                );
    }

    private File saveImageToFile(Uri imageUri) {
        ContentResolver resolver = getActivity().getContentResolver();
        File file = null;

        try {
            InputStream inputStream = resolver.openInputStream(imageUri);

            file = new File(getActivity().getFilesDir(), "image.jpg");


            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        } catch (Exception e) {
        }

        return file;
    }

    @SuppressLint("CheckResult")
    private void publishEvent() {


        apiService.createEmptyEvent("Bearer " + preferences.getToken())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        String jsonString = responseBody.string();

                        Gson gson = new Gson();
                        EventData eventData = gson.fromJson(jsonString, EventData.class);

                        String eventId = eventData.getId();
                        Log.i("APISERVICE", eventId + "");
                        putDataInsideEvent(eventId);

                    }
                });
    }

    @SuppressLint("CheckResult")
    private void putDataInsideEvent(String eventId) {

        UserEvent userEvent = new UserEvent();
        userEvent.setFullDescription(fullDescription + "");
        userEvent.setShortDescription(shortDescription + "");
        userEvent.setImageUrl("https://ap-dev.sportforall.gov.ua/api/v1/uploads/6dfa9ea6-123d-4588-8f75-290d91f8aea8");

        Observable<ResponseBody> observable = apiService.setDataEvent("Bearer " + preferences.getToken(), eventId, userEvent);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {

                        Log.i("APISERVICE", "Hkggk");
                        publishDataEvent(eventId);


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("APISERVICE", throwable.getMessage() + " HELLO WORLD" + eventId);
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void publishDataEvent(String eventToken) {

        Observable<ResponseBody> observable = apiService.publishDataEvent("Bearer " + preferences.getToken(), eventToken);
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {

                        Log.i("APISERVICE", "YES");

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("APISERVICE", throwable.getMessage() + " NOU");
                    }
                });

    }

    private void getDataFromViews() {
        eventName = String.valueOf(binding.editNameEvent.getText());
        // shortDescription = String.valueOf(binding.editShortDescription.getText());
        fullDescription = String.valueOf(binding.editFullDescription.getText());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                // Отримайте URI обрізаного зображення
                Log.i("CROPIMM", "WE ARE зображення");

                Uri croppedImageUri = result.getUri();
                binding.imageCover.setImageURI(croppedImageUri);
                // Використовуйте croppedImageUri за потребою

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                // Обробка помилок обрізання зображення

                Exception error = result.getError();
                Log.i("CROPIMM", error + "WE ARE зображення");
            }
        }
    }

    private void setSpiner() {

        String[] data = {
                getString(R.string.choose_type),
                getString(R.string.with_route),
                getString(R.string.simple),
                getString(R.string.online_training)};


        // Створити свій адаптер для Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(),
                R.layout.spinner_item_layout, // Ваш ресурс макету елемента списку
                data
        ) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);

                // Перший елемент буде відображений інакше
                if (position == 0) {
                    view = LayoutInflater.from(getContext()).inflate(R.layout.spinner_dropdown_item_layout, parent, false);
                    TextView textView = view.findViewById(android.R.id.text1);
                    textView.setText(data[position]);
                }

                return view;
            }
        };

// Встановити адаптер для Spinner
        binding.spinner.setAdapter(adapter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                binding.mapContainer.setVisibility(View.VISIBLE);
                binding.removePoint.setVisibility(View.GONE);
                typeOfEvent = i;

                if(typeOfEvent==1){
                    binding.removePoint.setVisibility(View.VISIBLE);
                }

                if(typeOfEvent==3){
                    binding.mapContainer.setVisibility(View.GONE);
                }

                Toast.makeText(getContext(), " rrr"+i, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getContext(), " NOTHING", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void choseTime(int year, int month, int day, TextView textView) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                (view, hourOfDay, minute) -> {
                    // Обробка вибору часу
                    textView.setText(formatDateTime(year, month, day, hourOfDay, minute));
                },
                5,
                20,
                false
        );
        timePickerDialog.show();
    }

    public static String formatDateTime(int year, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    private void addPoint() {

        GeoPoint centerCoordinates = (GeoPoint) binding.eventMap.getMapCenter();

        //GetAddressAsync getAddressAsync = new GetAddressAsync(centerCoordinates);
        //String ss = getAddressAsync.call();
        //binding.editAdress.setText(ss);

        setMarker(centerCoordinates);

    }


    private void setMarker(GeoPoint geoPoint) {
        // Очистка предыдущего маркера, если он существует
        if (centerMarker != null) {
            binding.eventMap.getOverlayManager().remove(centerMarker);
        }

        // Создание маркера
        Drawable icon = getResources().getDrawable(R.drawable.ic_pin_green);

        centerMarker = new Marker(binding.eventMap);
        centerMarker.setIcon(icon);
        centerMarker.setPosition(geoPoint);
        centerMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

        // Добавление маркера на карту
        binding.eventMap.getOverlayManager().add(centerMarker);
        binding.eventMap.invalidate(); // Перерисовка карты
    }

    private void getAddressFromCoordinates(GeoPoint geoPoint) {
        // Создание объекта GeocoderNominatim
        GeocoderNominatim geocoder = new GeocoderNominatim("user_agent");
        Log.i("Coordina", "Помилка при визначенні адреси " + geocoder);
        try {
            // Получение списка адресов по координатам
            List<android.location.Address> addresses = geocoder.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);

            Log.i("Coordina", "Помилка при визначенні адреси " + addresses);

            if (addresses != null && addresses.size() > 0) {
                android.location.Address address = addresses.get(0);
                String fullAddress = address.getAddressLine(0); // Получение полного адреса

                Log.i("Coordina", "Адреса точки: " + fullAddress);
                binding.editAdress.setText("");
                binding.editAdress.setText(fullAddress + "");

            } else {

                Log.i("Coordina", "Адреса точки не знайдено");
            }
        } catch (Exception e) {
            e.printStackTrace();

            Log.i("Coordina", "Помилка при визначенні адреси " + e.getMessage());
        }
    }

    private void drawRoute(List<GeoPoint> points) {
        Log.i("MYROUTE", points.size() + " MYSIZE");
        // Удаление предыдущего маршрута, если он существует
        //binding.eventMap.getOverlayManager().removeAll(Collections.singleton(Polyline::class.java));
        //binding.eventMap.getOverlayManager().removeAll(Polyline::class))
        //binding.eventMap.getOverlays().removeAll(Collections.singleton(Polyline::class.java))
        binding.eventMap.getOverlayManager().removeAll(Collections.singleton(Polyline.class));

        //binding.eventMap.getOverlayManager().removeAll(Collection<Polyline> collection)
        // binding.eventMap.getOverlayManager().overlays(). = binding.eventMap.overlayManager.overlays.filterNot { it is Polyline }


        // Построение маршрута
        Polyline route = new Polyline();
        route.setPoints(points);
        route.setColor(0xFF0000FF); // Синий цвет
        binding.eventMap.getOverlayManager().add(route);
        binding.eventMap.invalidate(); // Перерисовка карты

        Log.i("MYROUTE", points.size() + " MYSIZE");
    }

    private void clickForRoute() {
        startPoint = (GeoPoint) binding.eventMap.getMapCenter();
        // Инициализация списка точек маршрута
        routePoints.add(startPoint); // Добавление первой точки в маршрут

        drawRoute(routePoints);


    }

    public void updateUI(String adress){

        binding.editAdress.setText(adress);


    }

}
