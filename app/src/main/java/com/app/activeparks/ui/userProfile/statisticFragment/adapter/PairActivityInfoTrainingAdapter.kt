package com.app.activeparks.ui.userProfile.statisticFragment.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.ui.userProfile.model.PairActivityInfoTrainingItem
import com.app.activeparks.ui.userProfile.model.getUnitInfFirst
import com.app.activeparks.ui.userProfile.model.getUnitInfSecond
import com.app.activeparks.util.extention.DataHelper
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.visible
import com.technodreams.activeparks.databinding.ItemPairActivityTrainingBinding


/**
 * Created by O.Dziuba on 30.10.2023.
 */
class PairActivityInfoTrainingAdapter :
    RecyclerView.Adapter<PairActivityInfoTrainingAdapter.ActivityInfoTrainingItemVH>() {

    class ActivityInfoTrainingItemVH(binding: ItemPairActivityTrainingBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val callback = object : DiffUtil.ItemCallback<PairActivityInfoTrainingItem>() {
        override fun areItemsTheSame(
            oldItem: PairActivityInfoTrainingItem,
            newItem: PairActivityInfoTrainingItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: PairActivityInfoTrainingItem,
            newItem: PairActivityInfoTrainingItem
        ): Boolean {
            return oldItem.numberFirst == newItem.numberFirst
        }
    }

    val list = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityInfoTrainingItemVH {
        return ActivityInfoTrainingItemVH(
            ItemPairActivityTrainingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.currentList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ActivityInfoTrainingItemVH, position: Int) {
        val item = list.currentList[position]
        ItemPairActivityTrainingBinding.bind(holder.itemView).apply {
            tvTitleFirst.text = when (item.id) {
                5 -> {
                    DataHelper.formatDurationHour(item.numberFirst.toLong())
                }
                7 -> {
                    "${DataHelper.formatDuration(item.numberFirst.toLong())} ${item.unit}"
                }
                else -> {
                    item.getUnitInfFirst()
                }
            }
            if (item.numberSecond.isEmpty()) {
                tvTitleSecond.gone()
            } else {
                tvTitleSecond.visible()
                tvTitleSecond.text = item.getUnitInfSecond()
            }
            tvDescription.text = item.description
            ivIcon.setImageResource(item.img)
        }
    }
}