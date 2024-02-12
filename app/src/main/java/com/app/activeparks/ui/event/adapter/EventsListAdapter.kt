package com.app.activeparks.ui.event.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.ui.event.util.EventTypes
import com.app.activeparks.util.ChangeDateType
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ItemEventsBinding

class EventsListAdapter(
    private val event: (ItemEvent) -> Unit
) : RecyclerView.Adapter<EventsListAdapter.LevelOfActivityVH>() {

    class LevelOfActivityVH(binding: ItemEventsBinding) :
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
            ItemEventsBinding.inflate(
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
        ItemEventsBinding.bind(holder.itemView).apply {
            val context = tvTitle.context

            when(item.typeIdNew){
                EventTypes.ROUTE_TRAINING.type -> tvDescription.text =context.getString(R.string.with_route)
                EventTypes.SIMPLE_TRAINING.type -> tvDescription.text =context.getString(R.string.simple)
                EventTypes.ONLINE_TRAINING.type -> tvDescription.text =context.getString(R.string.online_training)
            }

            when(item.holdingStatusIdNew){
                EventTypes.EVENTS_DURING.type -> tvTitle.text =context.getString(R.string.event_during)
                EventTypes.EVENT_HAS_NOT_STARTED.type -> tvTitle.text =context.getString(R.string.event_has_not_started)
                EventTypes.EVENT_FINISHED.type -> tvTitle.text =context.getString(R.string.event_finished)
            }

            item?.imageUrlNew?.let {
                Glide
                    .with(photo.context)
                    .load(item.imageUrlNew)
                    .error(R.drawable.ic_prew)
                    .into(photo) }


            photo.setOnClickListener {
                event(item)
            }

            tvStartPoint.text = item.startAdressPoint
            tvStartPoint.text = item.title

            date.text = item.startsAtNew?.let { ChangeDateType.parseNewDateFormat(it) }

        }
    }
}