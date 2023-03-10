package com.app.activeparks.ui.notification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.app.activeparks.ui.clubs.ClubActivity;
import com.app.activeparks.ui.event.EventActivity;
import com.app.activeparks.ui.notification.adapter.EventsAdaper;
import com.app.activeparks.ui.notification.adapter.NotificationAdaper;
import com.app.activeparks.ui.profile.uservideo.UserAddVideoActivity;
import com.app.activeparks.ui.selectvideo.SelectVideoActivity;
import com.technodreams.activeparks.R;
import com.technodreams.activeparks.databinding.FragmentNotificationBinding;

public class NotificationFragment extends Fragment {

    private FragmentNotificationBinding binding;
    private NotificationViewModel viewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this, new NotificationModelFactory(getContext())).get(NotificationViewModel.class);

        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ViewPager2 listNotificationsHorizontal = binding.listNotificationsHorizontal;
        RecyclerView listNotifications = binding.listNotifications;

        listNotificationsHorizontal.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        viewModel.getSportEventsList().observe(getViewLifecycleOwner(), events -> {
            if (events.getItems().size() > 0) {
                binding.listNull.setVisibility(View.GONE);
            }

            listNotificationsHorizontal.setAdapter(new EventsAdaper(getActivity(), events).setOnCliclListener(new EventsAdaper.ParksAdaperListener() {
                @Override
                public void onInfo(String id) {
                    startActivity(new Intent(getActivity(), EventActivity.class).putExtra("id", id));
                }
            }));
        });

        viewModel.getNotifications().observe(getViewLifecycleOwner(), notifications -> {
            if (notifications.getItems().size() > 0) {
                binding.listNullTwo.setVisibility(View.GONE);
            }

            listNotifications.setAdapter(new NotificationAdaper(getActivity(), notifications.getItems()).setOnCliclListener(new NotificationAdaper.NotificationListener() {
                @Override
                public void onInfo(String url) {
                    if (url.length() > 36) {
                        String id = url.substring(url.length() - 36, url.length());
                        if (url.contains("fc")) {
                            startActivity(new Intent(getActivity(), ClubActivity.class).putExtra("id", id));
                        } else if (url.contains("fc-events")) {
                            startActivity(new Intent(getActivity(), EventActivity.class).putExtra("id", id));
                        } else if (url.contains("videoUserItem")) {
                            startActivity(new Intent(getActivity(), UserAddVideoActivity.class).putExtra("id", id));
                        }
                    }
                }
            }));
        });



        viewModel.update();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}