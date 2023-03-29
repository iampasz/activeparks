package com.app.activeparks.ui.participants;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.activeparks.data.model.user.User;
import com.app.activeparks.ui.participants.adapter.UsersAdaper;
import com.app.activeparks.ui.people.UserActivity;
import com.technodreams.activeparks.databinding.FragmentUsersBinding;

public class ParticipantsFragment extends Fragment {

    public ParticipantsFragment() {
    }

    public ParticipantsFragment(String id, boolean isAdmin, boolean event) {
        this.id = id;
        this.isAdmin = isAdmin;
        this.isEvent = event;
    }

    private FragmentUsersBinding binding;
    private ParticipantsViewModel mViewModel;
    private String id = null;
    private Boolean isAdmin = null;
    private Boolean isEvent = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this, new ParticipantsModelFactory(getContext())).get(ParticipantsViewModel.class);

        binding = FragmentUsersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (isEvent != false) {
            mViewModel.getEventUser(id, true);
        } else {
            if (isAdmin == true) {
                mViewModel.getClubsUser(id);
            } else {
                mViewModel.getClubsUser(id);
            }
        }

        initObserve();

        return root;
    }

    public void initObserve() {
        mViewModel.getUserHeads().observe(getViewLifecycleOwner(), item -> {
            if (item.getItems().size() > 0) {
                binding.listNull.setVisibility(View.GONE);
            }
            binding.listHeads.setAdapter(new UsersAdaper(getActivity(), item.getItems()).setOnClubsListener(new UsersAdaper.UsersListener() {
                @Override
                public void onInfo(String id) {
                    startActivity(new Intent(getActivity(), UserActivity.class).putExtra("id", id));
                }

                @Override
                public void approve(String user) {
                    mViewModel.approveUser(id, user);
                }

                @Override
                public void reject(String user) {
                    mViewModel.rejectUser(id, user);
                }
            }));
        });

        mViewModel.getUserMembers().observe(getViewLifecycleOwner(), item -> {
            if (item.getItems().size() > 0) {
                binding.listNullTwo.setVisibility(View.GONE);
            }
            binding.listMembers.setAdapter(new UsersAdaper(getActivity(), item.getItems()).setOnClubsListener(new UsersAdaper.UsersListener() {
                @Override
                public void onInfo(String id) {
                    startActivity(new Intent(getActivity(), UserActivity.class).putExtra("id", id));
                }

                @Override
                public void approve(String user) {
                    mViewModel.approveUser(id, user);
                }

                @Override
                public void reject(String user) {
                    mViewModel.rejectUser(id, user);
                }
            }));
        });

        mViewModel.getUserApplying().observe(getViewLifecycleOwner(), item -> {
            if (item.getItems().size() > 0) {
                binding.applying.setVisibility(View.VISIBLE);
            }

            binding.listApplying.setAdapter(new UsersAdaper(getActivity(), item.getItems()).setOnClubsListener(new UsersAdaper.UsersListener() {
                @Override
                public void onInfo(String id) {
                    startActivity(new Intent(getActivity(), UserActivity.class).putExtra("id", id));
                }

                @Override
                public void approve(String user) {
                    mViewModel.approveUser(id, user);
                }

                @Override
                public void reject(String user) {
                    mViewModel.rejectUser(id, user);
                }
            }));
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}