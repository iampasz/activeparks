package com.app.activeparks.ui.event.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.data.model.events.Event
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ItemEventBinding

class EventsCalendarListAdapter (private val eventList: List<Event>) :
    RecyclerView.Adapter<EventsCalendarListAdapter.EventViewHolder>() {

    class EventViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event) {
            binding.title.text = event.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEventBinding.inflate(inflater, parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentEvent = eventList[position]
        holder.bind(currentEvent)
    }

    override fun getItemCount() = eventList.size
}