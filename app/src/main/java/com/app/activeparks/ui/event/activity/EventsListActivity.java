package com.app.activeparks.ui.event.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.calendar.CalendarItem;
import com.app.activeparks.data.model.calendar.CalendarModel;
import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.model.sportevents.SportEvents;
import com.app.activeparks.data.repository.Repository;
import com.app.activeparks.data.storage.Preferences;
import com.app.activeparks.ui.event.util.EventModelFactory;

import com.app.activeparks.ui.event.fragments.FragmentEventCreate;
import com.app.activeparks.ui.event.adapter.EventsListAdaper;
import com.app.activeparks.ui.event.viewmodel.EventRouteViewModel;
import com.app.activeparks.ui.event.viewmodel.EventViewModel;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.technodreams.activeparks.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EventsListActivity extends AppCompatActivity implements LocationListener {

    private EventViewModel viewModel;
    Disposable disposable;

    private CalendarView calendarView;
    private RecyclerView listClubOwner;

    private LocationManager locationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events);
        overridePendingTransition(R.anim.start, R.anim.end);

        viewModel =
                new ViewModelProvider(this, new EventModelFactory(this)).get(EventViewModel.class);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        listClubOwner = findViewById(R.id.list_events);
        calendarView = findViewById(R.id.calendarView);
        ImageView createEventButton = findViewById(R.id.createEvent);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TabLayout selectFilter = findViewById(R.id.select_filter);
        selectFilter.setVisibility(View.VISIBLE);
        Objects.requireNonNull(selectFilter.getTabAt(1)).select();

        viewModel.getEventsList();
        viewModel.calendarEvent();
        findViewById(R.id.closed).setOnClickListener(v -> onBackPressed());

        createEventButton.setOnClickListener(view -> updateViewModelData());

        // listStatus.setText("Жодного запланованого заходу");
        viewModel.getSportEventsList().

                observe(this, this::setAdapter);

        viewModel.getCalendar().observe(this, this::setMapperAdapter);

        calendarView.setOnDayClickListener(eventDay -> {
            Calendar cal = eventDay.getCalendar();
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            viewModel.eventsDay(dateFormat.format(cal.getTime()));
        });

        selectFilter.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewModel.selectFilter(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void setMapperAdapter(CalendarModel calendarItem) {
        List<EventDay> days = new ArrayList<>();

        Calendar calendar;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (CalendarItem item : calendarItem.getItems()) {
            try {
                if (item.data() != null) {
                    calendar = Calendar.getInstance();
                    calendar.setTime(Objects.requireNonNull(sdf.parse(item.data())));
                    days.add(new EventDay(calendar, R.drawable.seekbar_drawable_mark));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        calendarView.setEvents(days);

    }

    public void setAdapter(SportEvents events) {
        if (events.getItems().size() > 0) {
            findViewById(R.id.list_null).setVisibility(View.GONE);
        }
        listClubOwner.setAdapter(new EventsListAdaper(
                this, events.getItems()).setOnEventListener(
                        new EventsListAdaper.EventsListener() {
            @Override
            public void onInfo(ItemEvent itemClub) {
                startActivity(new Intent(getBaseContext(), EventFragment.class).putExtra("id", itemClub.getId()));
            }

                    @Override
                    public void onOpenMaps(double lat, double lon) {
                        String uri = "https://www.google.com/maps/search/?api=1&query=" + lat + "," + lon;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(intent);
                    }
                }));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 50, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        viewModel.filterCordinate(latitude, longitude);
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


    private void openCreateEventFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.constrain_events_container, new FragmentEventCreate())
                .commit();
    }

    private EventRouteViewModel eventRouteViewModel;

    private void updateViewModelData() {
        eventRouteViewModel = new ViewModelProvider(this).get(EventRouteViewModel.class);

        Preferences preferences;
        Repository repository;

        preferences = new Preferences(this);
        preferences.setServer(true);
        repository = new Repository(preferences);

        disposable = repository.createEmptyEvent()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBody -> {

                            String jsonResponse = responseBody.string();
                            Gson gson = new Gson();
                            ItemEvent itemEvent = gson.fromJson(jsonResponse, ItemEvent.class);
                            eventRouteViewModel.updateItemEventData(itemEvent);
                            openCreateEventFragment();

                        }, error -> Toast.makeText(this, "Виникли проблеми, спробуйте пізніше", Toast.LENGTH_SHORT).show()
                );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}