package com.app.activeparks.ui.homeWithUser.fragments.clubs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.data.model.clubs.ItemClub
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ItemHomeClubsListBinding


/**
 * Created by O.Dziuba on 18.12.2023.
 */
class HomeClubsAdapter(
) : RecyclerView.Adapter<HomeClubsAdapter.LevelOfActivityVH>() {

    class LevelOfActivityVH(binding: ItemHomeClubsListBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val callback = object : DiffUtil.ItemCallback<ItemClub>() {
        override fun areItemsTheSame(oldItem: ItemClub, newItem: ItemClub): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ItemClub,
            newItem: ItemClub
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    val list = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelOfActivityVH {
        return LevelOfActivityVH(
            ItemHomeClubsListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.currentList.size

    override fun onBindViewHolder(holder: LevelOfActivityVH, position: Int) {
        val item = list.currentList[position]
        ItemHomeClubsListBinding.bind(holder.itemView).apply {
            tvDescription.text = item.name

            Glide
                .with(ivNews.context)
                .load(item.logoUrl)
                .error(R.drawable.ic_prew)
                .into(ivNews)

            tvLocation.text = "Location"
            tvPeoples.text = item.memberAmount.toString()
        }
    }
}