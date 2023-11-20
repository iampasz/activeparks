package com.app.activeparks.ui.maps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationRequest;
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
import com.app.activeparks.ui.dialog.BottomSearchDialog;
import com.app.activeparks.ui.maps.adapter.ParksAdaper;
import com.app.activeparks.ui.maps.adapter.ParksAdaper.*;
import com.app.activeparks.ui.dialog.BottomDialogActiveParkFragment;
import com.app.activeparks.ui.park.ParkActivity;
import com.app.activeparks.util.MapsViewController;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.technodreams.activeparks.R;
import com.technodreams.activeparks.databinding.FragmentMapsBinding;
import com.google.android.material.tabs.TabLayout;


public class MapsFragment extends Fragment implements View.OnClickListener {

    private FragmentMapsBinding binding;
    private MapsViewModel viewModel;

    private FusedLocationProviderClient mFusedLocationClient;
    public MapsViewController mapsViewController;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(MapsViewModel.class);

        binding = FragmentMapsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        binding.maxZoom.setOnClickListener(this);
        binding.minZoom.setOnClickListener(this);

        mapsViewController = new MapsViewController(binding.mapview, getContext());
        mapsViewController.homeView = true;
        viewModel.getSportsgroundList(50);

        setUpMapIfNeeded(getContext());

        final RecyclerView recyclerView = binding.mapsList;

        viewModel.getSportsground().observe(getViewLifecycleOwner(), sportsgrounds -> {

            ParksAdaper adapter = new ParksAdaper(getActivity(), sportsgrounds.getSportsground());
            adapter.setOnCliclListener(new ParksAdaperListener() {
                @Override
                public void onClick(Double lat, Double lon) {
                    String uri = "https://www.google.com/maps/search/?api=1&query=" + lat + "," + lon;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }

                @Override
                public void onInfoPark(ItemSportsground sportsground) {
                    startActivity(new Intent(getActivity(), ParkActivity.class).putExtra("id", sportsground.getId()));
                }
            });

            mapsViewController.setSportsgroundList(sportsgrounds);
            recyclerView.setAdapter(adapter);
        });

        binding.selectMap.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:
                        binding.selectList.setVisibility(View.GONE);
                        binding.selectMaps.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        binding.selectList.setVisibility(View.VISIBLE);
                        binding.selectMaps.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        mapsViewController.setOnCliclListener(new MapsViewController.MapsViewListener() {
            @Override
            public void onClick(ItemSportsground sportsground) {
                BottomDialogActiveParkFragment addPhotoBottomDialogFragment =
                        BottomDialogActiveParkFragment.newInstance().setSportsground(sportsground);
                addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(),
                        "add_photo_dialog_fragment");
            }

            @Override
            public void onPositionStatus(boolean status) {
                binding.actionSearch.setVisibility(status ? View.VISIBLE : View.INVISIBLE);
                if (status) binding.actionSearch.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fadein));
            }

            @Override
            public void onPosition(double latitude, double longitude) {
                viewModel.setGeolocation(latitude, longitude);
            }
        });

        binding.actionSearch.setOnClickListener(view -> {
            viewModel.getSportsgroundList(50);
            binding.actionSearch.setVisibility(View.INVISIBLE);
        });

        binding.locationAction.setOnClickListener(view -> {
            requestPermissions(getActivity());
        });

        binding.actionUpdateSearch.setOnClickListener(view -> {
            BottomSearchDialog addPhotoBottomDialogFragment =
                    BottomSearchDialog.newInstance().setOnCliclListener(new BottomSearchDialog.SearchDialogListener() {
                        @Override
                        public void onLong(double lat, double lon) {
                            mapsViewController.setPositionMap(lat, lon);
                            viewModel.setUpdateSportsgroundList(50, lat, lon);
                        }
                    });
            addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(),
                    "fragment_search");
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void setUpMapIfNeeded(Context ctx) {
        if (ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.max_zoom:
                mapsViewController.setZoomMax();
                break;
            case R.id.min_zoom:
                mapsViewController.setZoomMin();
                break;

        }
    }

    public void requestPermissions(Context activity) {
        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                && PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        } else {
            getLastLocation();
        }
    }

    @SuppressLint("MissingPermission")
    public void getLastLocation() {
        mFusedLocationClient.getCurrentLocation(LocationRequest.QUALITY_LOW_POWER, new CancellationToken() {
            @Override
            public boolean isCancellationRequested() {
                return false;
            }

            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mapsViewController.mylocation();
                    //binding.mapview.getController().animateTo(new GeoPoint(location.getLatitude(), location.getLongitude()));
                    viewModel.setMylocation(location.getLatitude(), location.getLongitude());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        }
    }

    @Override
    public void  onResume() {
        super.onResume();
        mapsViewController.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapsViewController.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapsViewController.onDestroy();
    }
}