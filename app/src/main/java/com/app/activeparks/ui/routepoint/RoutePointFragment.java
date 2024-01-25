package com.app.activeparks.ui.routepoint;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.points.RoutePoint;
import com.app.activeparks.ui.qr.QrCodeActivity;
import com.app.activeparks.ui.routepoint.adapter.PointListAdaper;
import com.app.activeparks.ui.scaner.ScanerBottomFragment;
import com.app.activeparks.util.LocalNotificationHelper;
import com.app.activeparks.util.MapsViewController;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.technodreams.activeparks.R;
import com.technodreams.activeparks.databinding.FragmentRoutePointBinding;

import org.jetbrains.annotations.Nullable;
import org.osmdroid.views.MapView;

public class RoutePointFragment extends DialogFragment implements LocationListener {

    private RoutePointViewModel viewModel;
    private LocationManager locationManager;
    public MapsViewController mapsViewController;
    public MapView mapView;

    public TextView status;

    public String id;

    public RoutePointFragment(String id) {
        this.id = id;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
        viewModel = new ViewModelProvider(this, new RoutePointModelFactory(getContext())).get(RoutePointViewModel.class);
        viewModel.initAudio(MediaPlayer.create(getActivity(), R.raw.soud));
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        com.technodreams.activeparks.databinding.FragmentRoutePointBinding binding = FragmentRoutePointBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        mapView = binding.mapview;
        status = binding.status;

        mapsViewController = new MapsViewController(mapView, getActivity());

        RecyclerView listPoint = binding.listPoint;

        binding.closed.setOnClickListener(v -> dismiss());

        viewModel.getRoutePoint(id);

        binding.qrAction.setOnClickListener(v -> {

            ScanerBottomFragment dialog = new ScanerBottomFragment();
            dialog.onListener(() -> viewModel.getUpdate());
            dialog.show(requireActivity().getSupportFragmentManager(),
                    "scaner");
        });

        viewModel.getRoutePoint().observe(getViewLifecycleOwner(), routePoint -> {
            if (!viewModel.updateMap) {
                mapsViewController.setRoutePint(viewModel.routePointsMap);
                viewModel.updateMap = true;
            }

            listPoint.setAdapter(new PointListAdaper(getActivity(), viewModel.routePoints, viewModel.isCoordinator).setOnEventListener(new PointListAdaper.EventsListener() {
                @Override
                public void onInfo(RoutePoint item) {
                    mapsViewController.selectMarker(item.getLocation().get(0), item.getLocation().get(1));
                }
                @Override
                public void onShowQrCode(String eventId, String pointId) {
                    startActivity(new Intent(getActivity(), QrCodeActivity.class)
                            .putExtra("pointId", pointId));
                }
            }));
        });

        viewModel.getNotification().observe(getViewLifecycleOwner(), msg -> LocalNotificationHelper.showNotification(getActivity(), "Активні парки", msg));

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getUpdate();
        onStartLocationUpdates();
    }

    public void onStartLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 1, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

//        status.setText(latitude+" | "+longitude);
        viewModel.routePoints(latitude, longitude);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

}