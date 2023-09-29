package com.app.activeparks.ui.active;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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


public class ActiveFragment extends Fragment {

    private FragmentActiveBinding binding;

    public MapsViewControler mapsViewControler;

    private LocationManager locationManager;

    private LocationListener locationListener;

    private Polyline line = new Polyline();

    private ActiveViewModel viewModel;


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
        locationManager.removeUpdates(locationListener);
        super.onDestroy();
    }

    private void startCheckLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 10, locationListener);
    }

    private void initListeners() {
        locationListener = location -> {
            GeoPoint geoPoint = new GeoPoint(location);
            line.addPoint(geoPoint);
            binding.mapview.getOverlayManager().add(line);
            binding.mapview.invalidate();
        };


        binding.actionSearch.setOnClickListener(view1 -> {
            startCheckLocation();
            binding.actionSearch.setVisibility(View.GONE);
            binding.actionClose.setVisibility(View.VISIBLE);
        });


        binding.actionClose.setOnClickListener(view1 -> {
            binding.actionSearch.setVisibility(View.VISIBLE);
            binding.actionClose.setVisibility(View.GONE);
            locationManager.removeUpdates(locationListener);
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
}