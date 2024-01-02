package com.app.activeparks.ui.homeWithUser.fragments.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.ui.event.util.EventController
import com.squareup.picasso.Picasso
import com.technodreams.activeparks.databinding.ItemEventBinding


/**
 * Created by O.Dziuba on 18.12.2023.
 */
class HomeEventsAdapter(
) : RecyclerView.Adapter<HomeEventsAdapter.LevelOfActivityVH>() {

    class LevelOfActivityVH(binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val callback = object : DiffUtil.ItemCallback<ItemEvent>() {
        override fun areItemsTheSame(oldItem: ItemEvent, newItem: ItemEvent): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ItemEvent,
            newItem: ItemEvent
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    val list = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelOfActivityVH {
        return LevelOfActivityVH(
            ItemEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.currentList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LevelOfActivityVH, position: Int) {
        val item = list.currentList[position]
        ItemEventBinding.bind(holder.itemView).apply {

            item.imageUrl?.let {
                Picasso.get().load(item.imageUrl).into(photo)
            }

            photo.setOnClickListener {
                EventController(photo.context).deleteEvent(item.id)
            }

            date.text = item.updatedAt
            nameEvent.text = item.title


            item.memberAmount?.let {
                counterText.text = "+${item.memberAmount - 1}"
            }
        }
    }
}