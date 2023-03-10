package com.app.activeparks.ui.event;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.event.RoutePoint;
import com.app.activeparks.ui.event.adapter.PointListAdaper;
import com.app.activeparks.ui.profile.EditProfileActivity;
import com.app.activeparks.ui.qr.QrCodeActivity;
import com.app.activeparks.ui.scaner.ScanerBottomFragment;
import com.app.activeparks.util.MapsViewControler;
import com.technodreams.activeparks.databinding.FragmentRoutePointBinding;

import org.osmdroid.views.MapView;

import java.util.List;

public class RoutePointFragment extends Fragment {

    private FragmentRoutePointBinding binding;

    private EventScanerListener eventScanerListener;
    private List<RoutePoint> routePoints;
    public MapsViewControler mapsViewControler;
    public MapView mapView;
    public Boolean isCoordinator;

    public RoutePointFragment(List<RoutePoint> routePoints, boolean isCoordinator, EventScanerListener eventScanerListener){
        this.eventScanerListener = eventScanerListener;
        this.routePoints = routePoints;
        this.isCoordinator = isCoordinator;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRoutePointBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        final RecyclerView listPoint = binding.listPoint;
        mapView = binding.mapview;

        mapsViewControler = new MapsViewControler(mapView, getActivity());


        mapsViewControler.setRoutePint(routePoints);

        listPoint.setAdapter(new PointListAdaper(getActivity(), routePoints).setOnEventListener(new PointListAdaper.EventsListener() {
            @Override
            public void onInfo(Boolean status) {
            }

            @Override
            public void onShowQrCode(String eventId, String pointId) {
                startActivity(new Intent(getActivity(), QrCodeActivity.class)
                        .putExtra("pointId", pointId));
            }
        }));



        binding.qrAction.setOnClickListener(v -> {

            ScanerBottomFragment dialog = new ScanerBottomFragment(eventScanerListener);
            dialog.show(getActivity().getSupportFragmentManager(),
                    "edit_profile");
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}