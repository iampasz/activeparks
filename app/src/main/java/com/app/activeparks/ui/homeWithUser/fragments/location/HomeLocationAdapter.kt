package com.app.activeparks.ui.homeWithUser.fragments.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.data.model.sportsgrounds.ItemSportsground
import com.app.activeparks.util.extention.toInfo
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ItemHomeLocationListBinding


/**
 * Created by O.Dziuba on 18.12.2023.
 */
class HomeLocationAdapter(
) : RecyclerView.Adapter<HomeLocationAdapter.LevelOfActivityVH>() {

    class LevelOfActivityVH(binding: ItemHomeLocationListBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val callback = object : DiffUtil.ItemCallback<ItemSportsground>() {
        override fun areItemsTheSame(
            oldItem: ItemSportsground,
            newItem: ItemSportsground
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ItemSportsground,
            newItem: ItemSportsground
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    val list = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelOfActivityVH {
        return LevelOfActivityVH(
            ItemHomeLocationListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.currentList.size

    override fun onBindViewHolder(holder: LevelOfActivityVH, position: Int) {
        val item = list.currentList[position]
        ItemHomeLocationListBinding.bind(holder.itemView).apply {
            tvTitle.text = item.title

            tvDescription.text = tvDescription.context.getString(
                R.string.tv_distance,
                String.format("%.2f", item.distanceToPoint)
            )

            Glide
                .with(ivNews.context)
                .load(item.photo)
                .error(R.drawable.ic_prew)
                .into(ivNews)
        }
    }
}