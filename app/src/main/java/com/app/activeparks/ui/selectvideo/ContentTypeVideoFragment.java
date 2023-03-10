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

public class ContentTypeVideoFragment extends Fragment implements View.OnClickListener {

    private View binding;

    public ContentVideoCallback callback;

    public static ContentTypeVideoFragment newInstance() {
        return new ContentTypeVideoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = inflater.inflate(R.layout.fragment_content_type_video, container, false);
        return binding;
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.findViewById(R.id.cloded).setOnClickListener((View v) -> {
            getActivity().finish();
        });

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
                select(0);
                break;
            case R.id.menu_2:
                select(1);
                break;
            case R.id.menu_3:
                select(2);
                break;
            case R.id.menu_4:
                select(3);
                break;
            case R.id.menu_5:
                select(4);
                break;
            case R.id.menu_6:
                select(5);
                break;
        }
    }
    private void select(int select){
        if (callback != null) {
            callback.onSelectType(select);
        }
    }

}
