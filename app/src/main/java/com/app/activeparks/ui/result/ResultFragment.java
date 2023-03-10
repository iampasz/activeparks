package com.app.activeparks.ui.result;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.technodreams.activeparks.databinding.FragmentResultBinding;

public class ResultFragment extends Fragment {

    private FragmentResultBinding binding;
    private ResultViewModel mViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this, new ResultModelFactory(getContext())).get(ResultViewModel.class);

        binding = FragmentResultBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final RecyclerView listResult = binding.listResult;

        mViewModel.getResult().observe(getViewLifecycleOwner(), result -> {
//            if (result.getSportsground().size() > 0) {
//                binding.listNull.setVisibility(View.GONE);
//            }
            //listNotifications.setAdapter(new ResultAdaper(getActivity(), notifications.getItems()));
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}