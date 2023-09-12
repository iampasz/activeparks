package com.app.activeparks.ui.active;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.sportsgrounds.ItemSportsground;
import com.app.activeparks.ui.dialog.BottomDialogActiveParkFragment;
import com.app.activeparks.ui.dialog.BottomSearchDialog;
import com.app.activeparks.ui.maps.MapsViewModel;
import com.app.activeparks.ui.maps.adapter.ParksAdaper;
import com.app.activeparks.ui.maps.adapter.ParksAdaper.ParksAdaperListener;
import com.app.activeparks.ui.park.ParkActivity;
import com.app.activeparks.util.MapsViewControler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.material.tabs.TabLayout;
import com.technodreams.activeparks.R;
import com.technodreams.activeparks.databinding.FragmentActiveBinding;
import com.technodreams.activeparks.databinding.FragmentMapsBinding;


public class ActiveFragment extends Fragment{

    private FragmentActiveBinding binding;
    private MapsViewModel viewModel;

    private FusedLocationProviderClient mFusedLocationClient;
    public MapsViewControler mapsViewControler;

    private LocationCallback locationCallback;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(MapsViewModel.class);

        binding = FragmentActiveBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mapsViewControler = new MapsViewControler(binding.mapview, getContext());
        mapsViewControler.homeView = true;

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(0); // Receive updates as soon as available
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(5); // Minimum distance in meters

        // Create the location callback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    // Handle the received location updates
                }
            }
        };


        return root;
    }


}