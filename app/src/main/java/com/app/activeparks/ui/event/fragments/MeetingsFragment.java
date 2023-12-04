package com.app.activeparks.ui.event.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.meetings.MeetingsModel;
import com.app.activeparks.ui.event.adapter.MeetingListAdaper;
import com.technodreams.activeparks.databinding.FragmentRecordBinding;

import java.util.List;

public class MeetingsFragment extends Fragment {

    private FragmentRecordBinding binding;
    private final List<MeetingsModel.MeetingItem> meetings;

    public MeetingsFragment(List<MeetingsModel.MeetingItem> meetings){
        this.meetings = meetings;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentRecordBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final RecyclerView listPoint = binding.listPoint;

        listPoint.setAdapter(new MeetingListAdaper(getActivity(), meetings).setOnMeetingListener(url -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)))));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}