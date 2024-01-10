package com.app.activeparks.ui.event.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.data.model.sportevents.EventList
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.ui.event.activity.EventListFragment
import com.app.activeparks.ui.event.adapter.EventsListAdapterKT
import com.app.activeparks.ui.event.interfaces.OnItemClickListener
import com.app.activeparks.ui.event.interfaces.ResponseCallBack
import com.app.activeparks.ui.event.util.EventController
import com.google.gson.Gson
import com.technodreams.activeparks.databinding.FragmentMyEventBinding

class MyEventsFragment : Fragment(), OnItemClickListener {

    lateinit var binding: FragmentMyEventBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMyEventBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val eventsListAdapter = EventsListAdapterKT(this)

        val setDataResponseSuccessful = object : ResponseCallBack {
            override fun load(responseFromApi: String) {

                val gson = Gson()
                val eventList =
                    gson.fromJson(
                        responseFromApi,
                        EventList::class.java
                    )

                val nameList: MutableList<ItemEvent> = mutableListOf()
                nameList.addAll(eventList.items)
                eventsListAdapter.differ.submitList(nameList)

                binding.listEvents.adapter = eventsListAdapter
            }
        }

        EventController(requireContext()).getMyEvents(setDataResponseSuccessful)

        binding.eventCalendar.setOnClickListener{
            startActivity(Intent(activity, EventListFragment::class.java))
        }
    }

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }
}