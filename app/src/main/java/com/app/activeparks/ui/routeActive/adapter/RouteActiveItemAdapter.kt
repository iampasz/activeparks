package com.app.activeparks.ui.routeActive.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.data.model.routeActive.*
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ItemRouteActiveBinding
import java.text.SimpleDateFormat
import java.util.Locale


class RouteActiveItemAdapter(private val onItemClick: (String) -> Unit, private val onRoutePoint: (PointsTrack) -> Unit) : RecyclerView.Adapter<RouteActiveItemAdapter.RouteActiveItemVH>() {

    class RouteActiveItemVH(binding: ItemRouteActiveBinding) :
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
            ItemRouteActiveBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun getItemCount() = list.currentList.size

    override fun onBindViewHolder(holder: RouteActiveItemVH, position: Int) {
        val item = list.currentList[position]
        ItemRouteActiveBinding.bind(holder.itemView).apply {
            tvTitle.text = item.name
            tvAdress.text = item.address
            tvSizeRoute.text = item.routeLength

            val serverData = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            item.dateStartRecord?.let {
                tvCreateRoute.text = SimpleDateFormat("dd.MM.yyyy", Locale("uk", "UA")).format(
                    serverData.parse(it)
                )
            }

            item.updatedAt?.let {
                tvDateUpdate.text = SimpleDateFormat("dd.MM.yyyy", Locale("uk", "UA")).format(
                    serverData.parse(it)
                )
            }

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