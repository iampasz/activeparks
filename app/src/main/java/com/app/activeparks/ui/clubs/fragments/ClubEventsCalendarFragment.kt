package com.app.activeparks.ui.clubs.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.activeparks.data.model.sportevents.EventList
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.technodreams.activeparks.databinding.FragmentCalendarClubBinding


class ClubEventsCalendarFragment : Fragment() {

    lateinit var binding: FragmentCalendarClubBinding
    private var nameList: MutableList<ItemEvent> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarClubBinding
            .inflate(inflater, container, false)
        return binding.root
    }
    

    @SuppressLint("NotifyDataSetChanged")
    fun setAdapter(events: EventList) {
        nameList = events.items as MutableList<ItemEvent>
    }
}