package com.app.activeparks.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.app.activeparks.MainActivity;
import com.app.activeparks.data.storage.Preferences;
import com.app.activeparks.ui.clubs.ClubActivity;
import com.app.activeparks.ui.clubs.ClubsListActivity;
import com.app.activeparks.ui.clubs.adapter.ClubsAdaper;
import com.app.activeparks.ui.event.activity.EventActivity;
import com.app.activeparks.ui.event.activity.EventListActivity2;
import com.app.activeparks.ui.home.adapter.HomeAdaper;
import com.app.activeparks.ui.home.adapter.HorizontalAdaperEvents;
import com.app.activeparks.ui.home.adapter.ParkHorizontalAdaper;
import com.app.activeparks.ui.news.NewsActivity;
import com.app.activeparks.ui.news.NewsFragment;
import com.app.activeparks.ui.park.ParkActivity;
import com.app.activeparks.util.FragmentInteface;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayoutMediator;
import com.technodreams.activeparks.R;
import com.technodreams.activeparks.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements LocationListener, SwipeRefreshLayout.OnRefreshListener {

    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private LocationManager locationManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this, new HomeModelFactory(getContext())).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);


        binding.swipeRefreshLayout.setOnRefreshListener(this);

        binding.listNews.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.listActivities.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.listParks.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        viewModel.setInitialData();
        viewModel.getParksList().observe(getViewLifecycleOwner(), events -> {
            if (events.getSportsground().size() > 0) {
                binding.listNull.setVisibility(View.GONE);
                binding.listParksTab.setVisibility(View.VISIBLE);
            }
            ParkHorizontalAdaper adapterEvents = new ParkHorizontalAdaper(getActivity(), events).setOnCliclListener(id -> startActivity(new Intent(getActivity(), ParkActivity.class).putExtra("id", id)));

            binding.listParks.setAdapter(adapterEvents);

            binding.listParks.setOffscreenPageLimit(3);

            binding.listParks.setPageTransformer((page, position) -> {
                float myOffset = position * -80;
                if (position <= 1) {
                    float scaleFactor = 1 - (0.20f * Math.abs(position));
                    page.setTranslationX(myOffset);
                    page.setScaleY(scaleFactor);
                    page.setZ(scaleFactor);
                } else {
                    page.setTranslationX(myOffset);
                    page.setZ(myOffset);
                }
            });

            new TabLayoutMediator(binding.listParksTab, binding.listParks, (tab, position) -> {

            }).attach();
        });

        viewModel.getNewsList().observe(getViewLifecycleOwner(), news -> {
            if (news != null && news.getItems().size() > 0) {
                binding.listNull2.setVisibility(View.GONE);
                binding.listNewsTab.setVisibility(View.VISIBLE);
            }

            HomeAdaper adapterNews = new HomeAdaper(getActivity(), news).setOnCliclListener(itemNews -> startActivity(new Intent(getActivity(), NewsActivity.class)
                    .putExtra("id", itemNews.getId())));

            binding.listNews.setAdapter(adapterNews);
            binding.listNews.setOffscreenPageLimit(3);

            binding.listNews.setPageTransformer((page, position) -> {
                float myOffset = position * -80;
                if (position <= 1) {
                    float scaleFactor = 1 - (0.20f * Math.abs(position));
                    page.setTranslationX(myOffset);
                    page.setScaleY(scaleFactor);
                    page.setZ(scaleFactor);
                } else {
                    page.setTranslationX(myOffset);
                    page.setZ(myOffset);
                }
            });

            new TabLayoutMediator(binding.listNewsTab, binding.listNews, (tab, position) -> {
            }).attach();
        });

        viewModel.getSportEventsList().observe(getViewLifecycleOwner(), items -> {
            if (items.size() > 0) {
                binding.listNull3.setVisibility(View.GONE);
                binding.listEventsTab.setVisibility(View.VISIBLE);
            }
            HorizontalAdaperEvents adapterEvents = new HorizontalAdaperEvents(getActivity(), items).setOnCliclListener(id -> startActivity(new Intent(getActivity(), EventActivity.class).putExtra("id", id)));

            binding.listActivities.setAdapter(adapterEvents);
            binding.listActivities.setOffscreenPageLimit(3);

            binding.listActivities.setPageTransformer((page, position) -> {
                float myOffset = position * -80;
                if (position <= 1) {
                    float scaleFactor = 1 - (0.20f * Math.abs(position));
                    page.setTranslationX(myOffset);
                    page.setScaleY(scaleFactor);
                    page.setZ(scaleFactor);
                } else {
                    page.setTranslationX(myOffset);
                    page.setZ(myOffset);
                }
            });

            new TabLayoutMediator(binding.listEventsTab, binding.listActivities, (tab, position) -> {

            }).attach();
        });


        if (viewModel.getUserAuth()) {
            binding.textClubs.setVisibility(View.VISIBLE);
            binding.listNull4.setVisibility(View.VISIBLE);
            binding.listClub.setVisibility(View.VISIBLE);
            binding.allClubs.setVisibility(View.VISIBLE);
            binding.allActivities.setVisibility(View.VISIBLE);
            binding.frameAvatar.setVisibility(View.VISIBLE);
            binding.autchStatus.setVisibility(View.GONE);
            viewModel.clubs();
        }

        viewModel.getClubs().observe(getViewLifecycleOwner(), clubs -> {
            if (clubs.size() > 0) {
                binding.listNull4.setVisibility(View.GONE);
            }
            binding.listClub.setAdapter(new ClubsAdaper(getContext(), clubs).setOnClubsListener(itemClub -> startActivity(new Intent(getContext(), ClubActivity.class)
                    .putExtra("id", itemClub.getId()))));
        });

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            binding.profileFilling.setProgress(user.getProfileFilling());
            Glide.with(this).load(user.getPhoto()).error(R.drawable.ic_prew).into(binding.imageUser);
        });

        viewModel.getLocation().observe(getViewLifecycleOwner(), location -> binding.statusLocation.setText(location));

        binding.panelUser.setOnClickListener(v -> {
            Preferences preferences = new Preferences(requireContext());
            if (preferences.getToken() == null || preferences.getToken().isEmpty()) {
                ((MainActivity) requireActivity()).getNavControllerMain().navigate(R.id.registration_user);
            } else {
                ((MainActivity) requireActivity()).getNavControllerMain().navigate(R.id.navigation_user);
            }
        });

        binding.allNews.setOnClickListener(v -> ((FragmentInteface) requireActivity()).show(new NewsFragment()));

        binding.allParsk.setOnClickListener(v -> ((FragmentInteface) requireActivity()).navigation(R.id.navigation_maps));

        binding.allActivities.setOnClickListener(v -> startActivity(new Intent(getActivity(), EventListActivity2.class)));

        binding.allClubs.setOnClickListener(v -> startActivity(new Intent(getActivity(), ClubsListActivity.class)));

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Request location updates
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 50, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        // Update the location text view with the new location
//        double latitude = location.getLatitude();
//        double longitude = location.getLongitude();
//        viewModel.location(latitude, longitude);
//        viewModel.getParks(latitude, longitude);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onRefresh() {
        binding.swipeRefreshLayout.setRefreshing(false);
        viewModel.setInitialData();
    }


}