package com.app.activeparks.ui.track.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.data.model.track.PointsTrack
import com.app.activeparks.ui.active.model.Direction
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ItemRoutePointsBinding


class RouteChangeAdapter(private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<RouteChangeAdapter.ItemRoutePointsVH>() {

    class ItemRoutePointsVH(binding: ItemRoutePointsBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val callback = object : DiffUtil.ItemCallback<PointsTrack>() {
        override fun areItemsTheSame(oldItem: PointsTrack, newItem: PointsTrack): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: PointsTrack,
            newItem: PointsTrack
        ): Boolean {
            return false
        }
    }

    val list = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemRoutePointsVH {
        return ItemRoutePointsVH(
            ItemRoutePointsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun submitData(newData: List<PointsTrack>) {
        list.submitList(newData)
    }

    override fun getItemCount() = list.currentList.size

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ItemRoutePointsVH, position: Int) {
        val item = list.currentList[position]
        ItemRoutePointsBinding.bind(holder.itemView).apply {
            tvNumber.text = "$position"

            if (item.turn == Direction.RIGHT.direction || item.turn == Direction.RIGHT.direction) {
                tvNumber.background =
                    holder.itemView.resources.getDrawable(R.drawable.button_ap, null)
            } else {
                tvNumber.background =
                    holder.itemView.resources.getDrawable(R.drawable.background_green, null)
            }

            tvNumber.setOnClickListener {
                onItemClick.invoke(position)
            }

        }
    }
}