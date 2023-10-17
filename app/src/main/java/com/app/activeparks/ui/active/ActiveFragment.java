package com.app.activeparks.ui.active;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.RoomDatabase;

import com.app.activeparks.data.storage.bd.AppDatabase;
import com.app.activeparks.ui.maps.MapsViewModel;
import com.app.activeparks.util.MapsViewControler;
import com.technodreams.activeparks.databinding.FragmentActiveBinding;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.List;


public class ActiveFragment extends Fragment implements LocationListener {

    private FragmentActiveBinding binding;

    public MapsViewControler mapsViewControler;

    private LocationManager locationManager;
    private Polyline line = new Polyline();

    private ActiveViewModel viewModel;

    private boolean isRunning = false;
    private long startTime = 0;

    private double distance = 0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentActiveBinding.inflate(inflater, container, false);

        viewModel =
                new ViewModelProvider(this).get(ActiveViewModel.class);

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);

        setCurrentLocation();
        initListeners();

        observes();
    }

    @Override
    public void onDestroy() {
        locationManager.removeUpdates(this);
        super.onDestroy();
    }

    private void startCheckLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, this);
    }

    private void startStopwatch() {
        if (!isRunning) {
            isRunning = true;
            startTime = SystemClock.elapsedRealtime();
            handler.postDelayed(updateTime, 0);
        }
    }


    private void stopStopwatch() {
        if (isRunning) {
            isRunning = false;
            handler.removeCallbacks(updateTime);
        }
    }

    private void resetStopwatch() {
        isRunning = false;
        handler.removeCallbacks(updateTime);
        binding.time.setText("00:00:00");
    }

    private final Handler handler = new Handler();
    private final Runnable updateTime = new Runnable() {
        @Override
        public void run() {
            long timeInMilliseconds = SystemClock.elapsedRealtime() - startTime;
            int seconds = (int) (timeInMilliseconds / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            seconds %= 60;
            minutes %= 60;

            binding.time.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            handler.postDelayed(this, 1000);
        }
    };

    private void initListeners() {
        binding.actionSearch.setOnClickListener(view1 -> {
            startCheckLocation();
            startStopwatch();
            binding.actionSearch.setVisibility(View.GONE);
            binding.actionClose.setVisibility(View.VISIBLE);
        });


        binding.actionClose.setOnClickListener(view1 -> {
            binding.actionSearch.setVisibility(View.VISIBLE);
            binding.actionClose.setVisibility(View.GONE);
            stopStopwatch();
            locationManager.removeUpdates(this);
            viewModel.insertGeoPointList(line.getActualPoints());
        });
    }

    private void observes() {
        viewModel.onSuccess.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                binding.mapview.getOverlayManager().removeIf(overlay -> overlay instanceof Polyline);
                line.getActualPoints().clear();
            }
        });

        viewModel.listsGeoPoint.observe(getViewLifecycleOwner(), lists -> {
            if (!lists.isEmpty()) {
                line.setPoints(lists.stream().findFirst().get());
                binding.mapview.getOverlayManager().add(line);
                binding.mapview.invalidate();
            }
        });
    }

    private void setCurrentLocation() {
        mapsViewControler = new MapsViewControler(binding.mapview, getContext());
        mapsViewControler.homeView = true;
    }

    private Location previousLocation;
    @Override
    public void onLocationChanged(@NonNull Location location) {
        GeoPoint geoPoint = new GeoPoint(location);
        if (previousLocation != null) {
            float distance = previousLocation.distanceTo(location);
            long timeElapsed = (location.getTime() - previousLocation.getTime()) / 1000; // Convert to seconds
            float speedMetersPerSecond = distance / timeElapsed;
            float speedKilometersPerHour = speedMetersPerSecond * 3.6f;
            float speedMinutesPerKilometer = 60.0f / speedKilometersPerHour;

            binding.itemThre.setText(String.valueOf(Math.round(speedMinutesPerKilometer * 10) / 10.0));
        } else  {
            previousLocation = location;
        }
        line.addPoint(geoPoint);
        binding.mapview.getOverlayManager().add(line);
        binding.mapview.invalidate();
        distance = distance + 0.1;
        binding.itemOne.setText(String.valueOf(Math.round(distance * 10) / 10.0));
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}