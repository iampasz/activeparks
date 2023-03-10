package com.app.activeparks.ui.event;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.app.activeparks.data.model.sportevents.ItemEvent;
import com.app.activeparks.data.model.sportevents.SportEvents;
import com.app.activeparks.ui.event.adapter.EventsListAdaper;
import com.app.activeparks.ui.home.HomeFragment;
import com.app.activeparks.util.FragmentInteface;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.technodreams.activeparks.R;
import com.technodreams.activeparks.databinding.FragmentEventsBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventsListFragment extends Fragment {

    private FragmentEventsBinding binding;
    private EventViewModel mViewModel;
    private String id = null;
    private CalendarView calendarView;
    private RecyclerView listClubOwner;

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

        listClubOwner = binding.listEvents;

        calendarView = binding.calendarView;

        if (id !=  null){
            binding.panelTop.setVisibility(View.GONE);
            mViewModel.getSportEvents(id);
        }else {
            mViewModel.getSportEvents();
            binding.panelTop.setVisibility(View.VISIBLE);
            binding.closed.setOnClickListener(v ->{
                ((FragmentInteface) getActivity()).show(new HomeFragment());
            });
        }

        mViewModel.getSportEventsList().observe(getViewLifecycleOwner(), events -> {
            Calendar cal = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        setAdapter(mViewModel.filterData(dateFormat.format(cal.getTime())));
            setMaperAdapter(events);
        });

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar cal = eventDay.getCalendar();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                setAdapter(mViewModel.filterData(dateFormat.format(cal.getTime())));
            }
        });


        return root;
    }


    public void setMaperAdapter(SportEvents events) {
        List<EventDay> days = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (ItemEvent itemEvent : events.getItems()){
            try {
                if (itemEvent.getStartsAt() != null) {
                    calendar = Calendar.getInstance();
                    calendar.setTime(sdf.parse(itemEvent.getStartsAt().substring(0, 10)));
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
            binding.listNull.setVisibility(View.GONE);
        }
        listClubOwner.setAdapter(new EventsListAdaper(getActivity(), events.getItems()).setOnEventListener(new EventsListAdaper.EventsListener() {
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