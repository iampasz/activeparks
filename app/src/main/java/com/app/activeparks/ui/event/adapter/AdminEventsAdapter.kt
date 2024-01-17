package com.app.activeparks.ui.event.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.squareup.picasso.Picasso
import com.technodreams.activeparks.R

class AdminEventsAdapter(private val context: Context, private val itemList: List<ItemEvent>) :
    RecyclerView.Adapter<AdminEventsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemEvent = itemList[position]
        holder.bind(itemEvent)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val photo: ImageView = itemView.findViewById(R.id.photo)

        fun bind(itemEvent: ItemEvent) {
            Picasso.get().load(itemEvent.imageUrl).into(photo)
        }
    }
}