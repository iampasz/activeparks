package com.app.activeparks.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.app.activeparks.data.model.clubs.ItemClub;
import com.app.activeparks.ui.clubs.ClubActivity;
import com.app.activeparks.ui.clubs.ClubsListActivity;
import com.app.activeparks.ui.clubs.adapter.ClubsAdaper;
import com.app.activeparks.ui.event.EventActivity;
import com.app.activeparks.ui.event.EventsListActivity;
import com.app.activeparks.ui.home.adapter.HomeAdaper;
import com.app.activeparks.ui.home.adapter.HorizontalAdaperEvents;
import com.app.activeparks.ui.home.adapter.ParkHorizontalAdaper;
import com.app.activeparks.ui.news.NewsActivity;
import com.app.activeparks.ui.news.NewsFragment;
import com.app.activeparks.ui.park.ParkActivity;
import com.app.activeparks.util.FragmentInteface;
import com.app.activeparks.util.ZoomOutPageTransformer;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.material.tabs.TabLayoutMediator;
import com.technodreams.activeparks.R;
import com.technodreams.activeparks.databinding.FragmentHomeBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

public class HomeFragment extends Fragment implements LocationListener {

    public FragmentInteface fragmentInteface;
    private FragmentHomeBinding binding;
    private HomeViewModel viewModel;
    private ViewPager2 listNewsView, listActivitiesView, listParksView;
    private RecyclerView listClub;
    private LocationManager locationManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this, new HomeModelFactory(getContext())).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        listNewsView = binding.listNews;
        listActivitiesView = binding.listActivities;
        listParksView = binding.listParks;
        listClub = binding.listClub;

        listNewsView.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        listActivitiesView.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        listParksView.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        viewModel.setInitialData();
        viewModel.getParksList().observe(getViewLifecycleOwner(), events -> {
            if (events.getSportsground().size() > 0) {
                binding.listNull.setVisibility(View.GONE);
                binding.listParksTab.setVisibility(View.VISIBLE);
            }
            ParkHorizontalAdaper adapterEvents = new ParkHorizontalAdaper(getActivity(), events).setOnCliclListener(new ParkHorizontalAdaper.ParksAdaperListener() {
                @Override
                public void onInfo(String id) {
                    startActivity(new Intent(getActivity(), ParkActivity.class).putExtra("id", id));
                }
            });

            listParksView.setPageTransformer((page, position) -> {

                    page.setTranslationX(page.getTranslationX() * position);
                    float scaleFactor = 1 - (0.15f * Math.abs(position));
                    page.setScaleY(scaleFactor);

            });
            listParksView.setAdapter(adapterEvents);

            new TabLayoutMediator(binding.listParksTab, listParksView, (tab, position) -> {

            }).attach();
        });

        viewModel.getNewsList().observe(getViewLifecycleOwner(), news -> {
            if (news != null && news.getItems().size() > 0) {
                binding.listNull2.setVisibility(View.GONE);
                binding.listNewsTab.setVisibility(View.VISIBLE);
            }
            HomeAdaper adapterNews = new HomeAdaper(getActivity(), news).setOnCliclListener(itemNews -> {
                startActivity(new Intent(getActivity(), NewsActivity.class)
                        .putExtra("id", itemNews.getId()));
            });

            listNewsView.setAdapter(adapterNews);

            new TabLayoutMediator(binding.listNewsTab, listNewsView, (tab, position) -> {
            }).attach();
        });

        viewModel.getSportEventsList().observe(getViewLifecycleOwner(), events -> {
            if (events.getItems().size() > 0) {
                binding.listNull3.setVisibility(View.GONE);
                binding.listEventsTab.setVisibility(View.VISIBLE);
            }
            HorizontalAdaperEvents adapterEvents = new HorizontalAdaperEvents(getActivity(), events).setOnCliclListener(new HorizontalAdaperEvents.ParksAdaperListener() {
                @Override
                public void onInfo(String id) {
                    startActivity(new Intent(getActivity(), EventActivity.class).putExtra("id", id));
                }
            });

            listActivitiesView.setAdapter(adapterEvents);

            new TabLayoutMediator(binding.listEventsTab, listActivitiesView, (tab, position) -> {

            }).attach();
        });


        if (viewModel.getUserAuth()) {
            binding.textClubs.setVisibility(View.VISIBLE);
            binding.listNull4.setVisibility(View.VISIBLE);
            binding.listClub.setVisibility(View.VISIBLE);
            binding.allClubs.setVisibility(View.VISIBLE);
            binding.allActivities.setVisibility(View.VISIBLE);
            binding.panelUser.setVisibility(View.VISIBLE);
            viewModel.clubs();
        }

        viewModel.getClubs().observe(getViewLifecycleOwner(), clubs -> {
            if (clubs.size() > 0) {
                binding.listNull4.setVisibility(View.GONE);
            }
            listClub.setAdapter(new ClubsAdaper(getContext(), clubs).setOnClubsListener(new ClubsAdaper.ClubsListener() {
                @Override
                public void onInfo(ItemClub itemClub) {
                    startActivity(new Intent(getContext(), ClubActivity.class)
                            .putExtra("id", itemClub.getId()));
                }
            }));
        });

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            binding.profileFilling.setProgress(user.getProfileFilling());
            Glide.with(this).load(user.getPhoto()).into(binding.imageUser);
        });

        viewModel.getLocation().observe(getViewLifecycleOwner(), location -> {
            binding.statusLocation.setText(location);
        });

        binding.allNews.setOnClickListener(v -> {
            ((FragmentInteface) getActivity()).show(new NewsFragment());
        });

        binding.allParsk.setOnClickListener(v -> {
            ((FragmentInteface) getActivity()).navigation(R.id.navigation_maps);
        });

        binding.allActivities.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), EventsListActivity.class));
        });

        binding.allClubs.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ClubsListActivity.class));
        });


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Request location updates
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 50, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onLocationChanged(Location location) {
        // Update the location text view with the new location
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        viewModel.location(latitude, longitude);
        viewModel.getParks(latitude, longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}