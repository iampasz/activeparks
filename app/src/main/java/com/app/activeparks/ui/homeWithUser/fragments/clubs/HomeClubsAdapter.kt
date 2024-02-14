package com.app.activeparks.ui.homeWithUser.fragments.clubs

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.data.model.clubs.ItemClub
import com.app.activeparks.util.GeocodingAsyncTask
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ItemHomeClubsListBinding


/**
 * Created by O.Dziuba on 18.12.2023.
 */
class HomeClubsAdapter(
    private val clubs: (ItemClub) -> Unit
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LevelOfActivityVH, position: Int) {

        holder.itemView.setOnClickListener {
            clubs(list.currentList[position])
        }

        val item = list.currentList[position]
        ItemHomeClubsListBinding.bind(holder.itemView).apply {
            tvDescription.text = item.name

            Glide
                .with(ivNews.context)
                .load(item.logoUrl)
                .error(R.drawable.ic_prew)
                .into(ivNews)



            if (item.location != null) {

                val coordinates = item.location as List<*>
                val latitude = coordinates[0] as Double
                val longitude = coordinates[1]as Double

                val adress = GeocodingAsyncTask().getCityName(ivNews.context, latitude, longitude)

                town.text = adress
            }



            when (item.memberAmount) {
                1 -> {
                    participants.text = "${item.memberAmount} учасник"
                }

                2, 3, 4 -> {
                    participants.text = "${item.memberAmount} учасники"
                }

                else -> {
                    participants.text = "${item.memberAmount} учасників"
                }
            }
        }
    }
}