package com.app.activeparks.ui.selectvideo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.activeparks.util.FragmenSelectInteface;
import com.technodreams.activeparks.R;

public class LevelVideoFragment extends Fragment implements View.OnClickListener {

    private View binding;
    private Boolean bottom = false;
    public ContentVideoCallback callback;

    public LevelVideoFragment(Boolean bottom) {
        this.bottom = bottom;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = inflater.inflate(R.layout.fragment_active_park, container, false);
        return binding;
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.findViewById(R.id.cloded).setOnClickListener((View v) -> {
            getActivity().finish();
        });

        if (bottom == true){
            binding.findViewById(R.id.menu_1).setVisibility(View.GONE);
            binding.findViewById(R.id.menu_2).setVisibility(View.GONE);
            binding.findViewById(R.id.menu_6).setVisibility(View.GONE);
            binding.findViewById(R.id.menu_3).setVisibility(View.VISIBLE);
            binding.findViewById(R.id.menu_4).setVisibility(View.VISIBLE);
            binding.findViewById(R.id.menu_5).setVisibility(View.VISIBLE);
        }else {
            binding.findViewById(R.id.menu_3).setVisibility(View.GONE);
            binding.findViewById(R.id.menu_4).setVisibility(View.GONE);
            binding.findViewById(R.id.menu_5).setVisibility(View.GONE);
        }

        binding.findViewById(R.id.menu_1).setOnClickListener(this);
        binding.findViewById(R.id.menu_2).setOnClickListener(this);
        binding.findViewById(R.id.menu_3).setOnClickListener(this);
        binding.findViewById(R.id.menu_4).setOnClickListener(this);
        binding.findViewById(R.id.menu_5).setOnClickListener(this);
        binding.findViewById(R.id.menu_6).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_1:
                select(1);
                break;
            case R.id.menu_2:
                select(2);
                break;
            case R.id.menu_3:
                select(2);
                break;
            case R.id.menu_4:
                select(1);
                break;
            case R.id.menu_5:
                select(0);
                break;
            case R.id.menu_6:
                select(0);
                break;
        }
    }

    private void select(int level){
        if (callback != null) {
            callback.onSelectLevel(level);
        }
    }
}