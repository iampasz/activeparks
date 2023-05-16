package com.app.activeparks.ui.event;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.calendar.CalendarItem;
import com.app.activeparks.data.model.calendar.CalendarModel;
import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.model.sportevents.SportEvents;
import com.app.activeparks.ui.event.adapter.EventsListAdaper;
import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.technodreams.activeparks.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventsListActivity extends AppCompatActivity {

    private EventViewModel viewModel;
    private String id = null;
    private CalendarView calendarView;
    private TextView listStatus;
    private RecyclerView listClubOwner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events);
        overridePendingTransition(R.anim.start, R.anim.end);

        viewModel =
                new ViewModelProvider(this, new EventModelFactory(this)).get(EventViewModel.class);

        listClubOwner = findViewById(R.id.list_events);

        calendarView = findViewById(R.id.calendarView);

        listStatus = findViewById(R.id.list_null);

        viewModel.getEventsList();
        viewModel.calendarEvent();
        findViewById(R.id.panel_top).setVisibility(View.VISIBLE);
        findViewById(R.id.title).setVisibility(View.GONE);
        findViewById(R.id.closed).setOnClickListener(v -> {
            onBackPressed();
        });

        viewModel.getSportEventsList().observe(this, events -> {
            setAdapter(events);
            listStatus.setText("Жодного запланованого заходу");
        });

        viewModel.getCalendar().observe(this, events -> {
            setMaperAdapter(events);
        });

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar cal = eventDay.getCalendar();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                viewModel.eventsDay(dateFormat.format(cal.getTime()));
            }
        });

    }

    public void setMaperAdapter(CalendarModel calendarItem) {
        List<EventDay> days = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (CalendarItem item : calendarItem.getItems()) {
            try {
                if (item.data() != null) {
                    calendar = Calendar.getInstance();
                    calendar.setTime(sdf.parse(item.data()));
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
        listClubOwner.setAdapter(new EventsListAdaper(this, events.getItems()).setOnEventListener(new EventsListAdaper.EventsListener() {
            @Override
            public void onInfo(ItemEvent itemClub) {
                startActivity(new Intent(getBaseContext(), EventActivity.class).putExtra("id", itemClub.getId()));
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
}