package com.app.activeparks.ui.track.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.data.model.track.TrackResponse
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ItemTrackBinding
import java.text.SimpleDateFormat
import java.util.Locale


/**
 * Created by O.Dziuba on 30.10.2023.
 */
class TrackItemAdapter(private val onItemClick: (String) -> Unit) : RecyclerView.Adapter<TrackItemAdapter.TrackItemVH>() {

    class TrackItemVH(binding: ItemTrackBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val callback = object : DiffUtil.ItemCallback<TrackResponse>() {
        override fun areItemsTheSame(oldItem: TrackResponse, newItem: TrackResponse): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: TrackResponse,
            newItem: TrackResponse
        ): Boolean {
            return false
        }
    }

    val list = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackItemVH {
        return TrackItemVH(
            ItemTrackBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun getItemCount() = list.currentList.size

    override fun onBindViewHolder(holder: TrackItemVH, position: Int) {
        val item = list.currentList[position]
        ItemTrackBinding.bind(holder.itemView).apply {
            tvTitle.text = item.name
            tvAdress.text = item.address
            tvSizeRoute.text = item.routeLength

            ivStatus.setImageResource(
                if (item.isTrackActive == true) {
                    R.drawable.ic_it_is_used
                } else {
                    R.drawable.is_not_stok_track
                }
            )

            val serverData = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            item.dateStartRecord?.let {
                tvCreateRoute.text = SimpleDateFormat("dd.MM.yyyy", Locale("uk", "UA")).format(
                    serverData.parse(it)
                )
            }

            item.dateStartRecord?.let {
                tvStartRoute.text = SimpleDateFormat("HH:mm", Locale("uk", "UA")).format(
                    serverData.parse(it)
                )
            }

            item.dateStartRecord?.let {
                tvFinishRoute.text = SimpleDateFormat("HH:mm", Locale("uk", "UA")).format(
                    serverData.parse(it)
                )
            }

            Glide.with(ivLogo.context).load(item.coverType).error(R.drawable.ic_prew)
                .into(ivLogo)

            holder.itemView.setOnClickListener {
                onItemClick.invoke(item.id)
            }

        }
    }
}