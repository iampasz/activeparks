package com.app.activeparks.ui.participants;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.activeparks.data.model.user.User;
import com.app.activeparks.ui.participants.adapter.UsersAdaper;
import com.app.activeparks.ui.people.UserActivity;
import com.app.activeparks.ui.profile.EditProfileActivity;
import com.app.activeparks.ui.support.SupportActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.technodreams.activeparks.R;
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
    private ParticipantsViewModel viewModel;
    private String id = null;
    private Boolean isAdmin = false;
    private Boolean isEvent = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this, new ParticipantsModelFactory(getContext())).get(ParticipantsViewModel.class);

        binding = FragmentUsersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel.setIsEvent(isEvent);

        update();

        initObserve();

        return root;
    }

    public void update(){
        if (isEvent != false) {
            viewModel.getEventUser(id, true);
            if (isAdmin == true) {
                viewModel.getEventUserApplying(id);
            }
        } else {
            viewModel.getClubsUser(id);
            if (isAdmin == true) {
                viewModel.getClubsUserApplying(id);
            }
        }
    }

    public void initObserve() {
        viewModel.getUserHeads().observe(getViewLifecycleOwner(), item -> {
            binding.listNull.setVisibility(item.getItems().size() > 0 ? View.GONE  : View.VISIBLE);
            binding.listHeads.setAdapter(new UsersAdaper(getActivity(), item.getItems(), true, isAdmin ? 1 : 0).setOnClubsListener(new UsersAdaper.UsersListener() {
                @Override
                public void onInfo(String id) {
                    startActivity(new Intent(getActivity(), UserActivity.class).putExtra("id", id));
                }

                @Override
                public void approve(String user) {
                }

                @Override
                public void reject(String user) {
                    dialogShow(user, true);
                }
            }));
        });

        viewModel.getUserMembers().observe(getViewLifecycleOwner(), item -> {
            binding.listTitleTwo.setVisibility(item.getItems().size() > 0 ? View.VISIBLE  : View.GONE);
            binding.listMembers.setAdapter(new UsersAdaper(getActivity(), item.getItems(), true, isAdmin ? 1 : 0).setOnClubsListener(new UsersAdaper.UsersListener() {
                @Override
                public void onInfo(String id) {
                    startActivity(new Intent(getActivity(), UserActivity.class).putExtra("id", id));
                }

                @Override
                public void approve(String user) {
                }

                @Override
                public void reject(String user) {
                    dialogShow(user, false);
                }
            }));
        });

        viewModel.getUserApplying().observe(getViewLifecycleOwner(), item -> {
            binding.applying.setVisibility(item.getItems().size() > 0 ? View.VISIBLE  : View.GONE);

            binding.listApplying.setAdapter(new UsersAdaper(getActivity(), item.getItems(), true, isAdmin ? 2  : 0).setOnClubsListener(new UsersAdaper.UsersListener() {
                @Override
                public void onInfo(String id) {
                    startActivity(new Intent(getActivity(), UserActivity.class).putExtra("id", id));
                }

                @Override
                public void approve(String user) {
                    viewModel.acceptUser(id, user);
                    update();
                }

                @Override
                public void reject(String user) {
                    viewModel.rejectUser(id, user);
                    update();
                }
            }));
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void dialogShow(String userId, boolean type) {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext(), R.style.CustomBottomSheetDialogTheme);
        dialog.setContentView(R.layout.dialog_participants);

        TextView updateAction = dialog.findViewById(R.id.update_action);
        updateAction.setText(type == false ? "Зробити організатором" : "Зняти організатора");
        updateAction.setOnClickListener(view -> {
            if (type == false) {
                viewModel.setActing(id, userId);
            }else {
                viewModel.removeActing(id, userId);
            }
            dialog.dismiss();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    update();
                }
            }, 500);
        });
        TextView removeAction = dialog.findViewById(R.id.remove_action);
        removeAction.setOnClickListener(view -> {
            viewModel.removeUser(id, userId);
            dialog.dismiss();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    update();
                }
            }, 500);
        });

        TextView cancel = dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
    }
}