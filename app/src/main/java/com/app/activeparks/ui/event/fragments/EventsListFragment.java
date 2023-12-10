package com.app.activeparks.ui.event.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.activeparks.data.model.calendar.CalendarItem;
import com.app.activeparks.data.model.calendar.CalendarModel;
import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.model.sportevents.SportEvents;
import com.app.activeparks.ui.event.activity.EventActivity;
import com.app.activeparks.ui.event.adapter.EventsListAdaper;
import com.app.activeparks.ui.event.util.EventModelFactory;
import com.app.activeparks.ui.event.viewmodel.EventViewModel;
import com.applandeo.materialcalendarview.EventDay;
import com.technodreams.activeparks.R;
import com.technodreams.activeparks.databinding.FragmentEventsBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class EventsListFragment extends Fragment {

    private FragmentEventsBinding binding;
    private EventViewModel mViewModel;
    private String id = null;

    public EventsListFragment(){}

    public EventsListFragment(String id){
        this.id = id;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this, new EventModelFactory(getContext())).get(EventViewModel.class);

        binding = FragmentEventsBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        binding.titleText2.setVisibility(View.GONE);
        mViewModel.calendarEvent(id);
        mViewModel.getEventsList(id);

        mViewModel.getCalendar().observe(getViewLifecycleOwner(), this::setMapperAdapter);

        mViewModel.getSportEventsList().observe(getViewLifecycleOwner(), this::setAdapter);

        binding.calendarView.setOnDayClickListener(eventDay -> {
            Calendar cal = eventDay.getCalendar();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            mViewModel.getEvents(dateFormat.format(cal.getTime()), id);
        });


        return root;
    }


    public void setMapperAdapter(CalendarModel calendarItem) {
        List<EventDay> days = new ArrayList<>();

        Calendar calendar;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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

        binding.calendarView.setEvents(days);
    }

    public void setAdapter(SportEvents events) {
        if (events.getItems().size() > 0) {
            binding.titleText2.setVisibility(View.GONE);
        }
        binding.listEvents.setAdapter(new EventsListAdaper(getActivity(), events.getItems()).setOnEventListener(new EventsListAdaper.EventsListener() {
            @Override
            public void onInfo(ItemEvent itemClub) {
                startActivity(new Intent(getActivity(), EventActivity.class).putExtra("id", itemClub.getId()));
            }

            @Override
            public void onOpenMaps(double lat, double lon) {

            }
        }));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}