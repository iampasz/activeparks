package com.app.activeparks.ui.event.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.data.model.sportevents.EventList
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.ui.event.activity.EventListActivity2
import com.app.activeparks.ui.event.adapter.BaseAdapter
import com.app.activeparks.ui.event.interfaces.RemoveItemPosition
import com.app.activeparks.ui.event.interfaces.ResponseCallBack
import com.app.activeparks.ui.event.util.EventController
import com.google.gson.Gson
import com.technodreams.activeparks.databinding.FragmentMyEventBinding

class MyEventsFragment : Fragment() {

    lateinit var binding: FragmentMyEventBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyEventBinding.inflate(inflater, container, false)

        return binding.getRoot()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val removeItem = object : RemoveItemPosition {

            override fun removePosition(position: Int) {

            }
        }

        val baseAdapter = BaseAdapter()

        val setDataResponseSuccessful = object : ResponseCallBack {
            override fun load(responseCallBack: String) {

                val gson = Gson()
                val eventList =
                    gson.fromJson(
                        responseCallBack,
                        EventList::class.java
                    )

                val nameList: MutableList<ItemEvent> = mutableListOf()
                nameList.addAll(eventList.items)

                baseAdapter.differ.submitList(nameList)

                binding.listEvents.adapter = baseAdapter
            }

        }

        EventController(requireContext()).getMyEvents(setDataResponseSuccessful)


        binding.eventCalendar.setOnClickListener{
            startActivity(Intent(activity, EventListActivity2::class.java))
        }
    }


}