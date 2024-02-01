package com.app.activeparks.ui.routeActive.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.data.model.routeActive.RouteActiveResponse
import com.app.activeparks.data.model.track.PointsTrack
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.visible
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ItemMapListBinding
import com.technodreams.activeparks.databinding.ItemRouteActiveBinding
import java.text.SimpleDateFormat
import java.util.Locale

class FavoritesRouteActiveItemAdapter(private val onItemClick: (String) -> Unit, private val onRoutePoint: (PointsTrack) -> Unit) : RecyclerView.Adapter<FavoritesRouteActiveItemAdapter.RouteActiveItemVH>() {

        class RouteActiveItemVH(binding: ItemMapListBinding) :
            RecyclerView.ViewHolder(binding.root)

        private val callback = object : DiffUtil.ItemCallback<RouteActiveResponse>() {
            override fun areItemsTheSame(oldItem: RouteActiveResponse, newItem: RouteActiveResponse): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: RouteActiveResponse,
                newItem: RouteActiveResponse
            ): Boolean {
                return false
            }
        }

        val list = AsyncListDiffer(this, callback)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteActiveItemVH {
            return RouteActiveItemVH(
                ItemMapListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }


        override fun getItemCount() = list.currentList.size

        override fun onBindViewHolder(holder: RouteActiveItemVH, position: Int) {
            val item = list.currentList[position]
            ItemMapListBinding.bind(holder.itemView).apply {
                tvTitle.text = item.name
                tvAddress.text = item.address

                Glide.with(ivLogo.context).load(item.coverType).error(R.drawable.ic_prew)
                    .into(ivLogo)

                holder.itemView.setOnClickListener {
                    onItemClick.invoke(item.id)
                }

                ivRoute.setOnClickListener {
                    item.pointsActiveRoutes?.get(0)?.let {
                        onRoutePoint.invoke(it)
                    }
                }
            }
        }
    }