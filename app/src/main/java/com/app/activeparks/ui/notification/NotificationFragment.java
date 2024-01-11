package com.app.activeparks.ui.notification;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.app.activeparks.ui.clubs.ClubActivity;
import com.app.activeparks.ui.event.activity.EventFragment;
import com.app.activeparks.ui.notification.adapter.EventsAdaper;
import com.app.activeparks.ui.notification.adapter.NotificationAdaper;
import com.app.activeparks.ui.profile.uservideo.UserAddVideoActivity;
import com.app.activeparks.util.FragmentInteface;
import com.bumptech.glide.Glide;
import com.technodreams.activeparks.R;
import com.technodreams.activeparks.databinding.FragmentNotificationBinding;

public class NotificationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentNotificationBinding binding;
    private NotificationViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this, new NotificationModelFactory(getContext())).get(NotificationViewModel.class);

        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ViewPager2 listNotificationsHorizontal = binding.listNotificationsHorizontal;
        ViewPager2 listRaiting = binding.listRaiting;
        RecyclerView listNotifications = binding.listNotifications;

        binding.swipeRefreshLayout.setOnRefreshListener(this);

        listNotificationsHorizontal.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        viewModel.getSportEventsList().observe(getViewLifecycleOwner(), events -> {
            if (events.getItems().size() > 0) {
                binding.titleEvent.setVisibility(View.VISIBLE);
                listNotificationsHorizontal.setVisibility(View.VISIBLE);
            }

            listNotificationsHorizontal.setAdapter(new EventsAdaper(getActivity(), events, false).setOnCliclListener(id -> startActivity(new Intent(getActivity(), EventFragment.class).putExtra("id", id))));
        });

        viewModel.getSportRaitingEventsList().observe(getViewLifecycleOwner(), events -> {
            if (events.getItems() != null && events.getItems().size() > 0) {
                binding.titleRaiting.setVisibility(View.VISIBLE);
                listRaiting.setVisibility(View.VISIBLE);
            }else {
                return;
            }

            listRaiting.setAdapter(new EventsAdaper(getActivity(), events, true).setOnCliclListener(id -> startActivity(new Intent(getActivity(), EventFragment.class).putExtra("id", id))));
        });

        viewModel.getNotifications().observe(getViewLifecycleOwner(), notifications -> {
            if (notifications.getItems().size() > 0) {
                binding.listNullTwo.setVisibility(View.GONE);
            }

            listNotifications.setAdapter(new NotificationAdaper(getActivity(), notifications.getItems()).setOnCliclListener(url -> {
                if (url.length() > 36) {
                    String id = url.substring(url.length() - 36);
                    if (url.contains("fc")) {
                        startActivity(new Intent(getActivity(), ClubActivity.class).putExtra("id", id));
                    } else if (url.contains("fc-events")) {
                        startActivity(new Intent(getActivity(), EventFragment.class).putExtra("id", id));
                    } else if (url.contains("videoUserItem")) {
                        startActivity(new Intent(getActivity(), UserAddVideoActivity.class).putExtra("id", id));
                    }
                }
            }));
        });

        if (viewModel.getUserAuth()) {
            binding.frameAvatar.setVisibility(View.VISIBLE);
            binding.autchStatus.setVisibility(View.GONE);
        }

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            binding.profileFilling.setProgress(user.getProfileFilling());
            Glide.with(this).load(user.getPhoto()).error(R.drawable.ic_prew).into(binding.imageUser);
        });

        binding.panelUser.setOnClickListener(v -> ((FragmentInteface) requireActivity()).navigation(R.id.navigation_user));
        viewModel.update();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onRefresh() {
        binding.swipeRefreshLayout.setRefreshing(false);
        viewModel.update();
    }
}