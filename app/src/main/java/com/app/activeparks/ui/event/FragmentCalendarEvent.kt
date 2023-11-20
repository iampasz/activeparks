package com.app.activeparks.ui.event

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.data.model.events.EventData
import com.app.activeparks.data.model.events.EventList
import com.app.activeparks.data.network.ApiService
import com.app.activeparks.data.repository.Repository
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.ui.event.adapter.EventsCalendarListAdapter
import com.google.gson.Gson
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.FragmentCalendarEventBinding
import com.technodreams.activeparks.databinding.FragmentEventCreateBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

//https://ap-dev.sportforall.gov.ua/api/v1/sport-events/
// day?offset=0&filters[startsFrom]=2023-11-20&filters[startsTo]=2023-11-20&sort[startsAt]=asc

//https://ap-dev.sportforall.gov.ua/api/v1/sport-events?offset=0&limit=10&filters[startsFrom]=2023-11-20&sort[title]=desc
//https://ap-dev.sportforall.gov.ua/api/v1/sport-events?offset=0&limit=10&sort[sort_name]=value&filters[filter_name]=value


class FragmentCalendarEvent : Fragment() {

    lateinit var binding: FragmentCalendarEventBinding

    lateinit var repository: Repository;
    lateinit var preferences: Preferences;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCalendarEventBinding
            .inflate(inflater, container, false);

        return binding.root;

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferences = Preferences(getContext());
        preferences.setServer(true);
        repository = Repository(preferences);
        binding.createEventButton.setOnClickListener{createNewEvent()}
        binding.backButton.setOnClickListener{closeFragment()}

        getEventsList();
    }

    @SuppressLint("CheckResult")
    fun getEventsList(){
        repository.getAllEventsPublished().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { responseBody ->
                val jsonString = responseBody.string()

                Log.i("MYEVENTS_LIST", jsonString+" there")
                val gson = Gson()
                val eventData = gson.fromJson(jsonString, EventList::class.java)
                //val events = eventData.items

                Log.i("MYEVENTS_LIST", "${eventData.total} there")
                //Log.i("MYEVENTS_LIST", "${events.size} events")

                val layoutManager = LinearLayoutManager(requireContext())
                val adapter = EventsCalendarListAdapter(eventData.items)
                binding.rvList.layoutManager = layoutManager
                binding.rvList.adapter = adapter

            }
    }

    fun createNewEvent(){
        parentFragmentManager.beginTransaction().add(R.id.fragment_container_user, FragmentEventCreate()).commit()
    }

    fun closeFragment(){
        parentFragmentManager.beginTransaction().remove(this).commit();
    }

}