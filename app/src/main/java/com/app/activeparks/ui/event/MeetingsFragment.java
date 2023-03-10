package com.app.activeparks.ui.event;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.event.RoutePoint;
import com.app.activeparks.data.model.meetings.MeetingsModel;
import com.app.activeparks.ui.event.adapter.MeetingListAdaper;
import com.app.activeparks.ui.event.adapter.PointListAdaper;
import com.app.activeparks.ui.qr.QrCodeActivity;
import com.app.activeparks.util.MapsViewControler;
import com.technodreams.activeparks.databinding.FragmentRoutePointBinding;

import org.osmdroid.views.MapView;

import java.util.List;

public class MeetingsFragment extends Fragment {

    private FragmentRoutePointBinding binding;
    private List<MeetingsModel.MeetingItem> meetings;

    public MeetingsFragment(List<MeetingsModel.MeetingItem> meetings){
        this.meetings = meetings;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRoutePointBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final RecyclerView listPoint = binding.listPoint;

        listPoint.setAdapter(new MeetingListAdaper(getActivity(), meetings).setOnMeetingListener(new MeetingListAdaper.MeetingListener() {
            @Override
            public void onInfo(String url) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        }));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}