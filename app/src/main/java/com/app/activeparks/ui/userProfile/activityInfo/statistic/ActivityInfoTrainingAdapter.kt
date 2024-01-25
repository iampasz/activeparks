package com.app.activeparks.ui.userProfile.activityInfo.statistic

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import com.app.activeparks.ui.active.model.getUnitInf
import com.app.activeparks.util.extention.DataHelper
import com.technodreams.activeparks.databinding.ItemPairActivityTrainingBinding


/**
 * Created by O.Dziuba on 30.10.2023.
 */
class ActivityInfoTrainingAdapter :
    RecyclerView.Adapter<ActivityInfoTrainingAdapter.ActivityInfoTrainingItemVH>() {

    class ActivityInfoTrainingItemVH(binding: ItemPairActivityTrainingBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val callback = object : DiffUtil.ItemCallback<ActivityInfoTrainingItem>() {
        override fun areItemsTheSame(
            oldItem: ActivityInfoTrainingItem,
            newItem: ActivityInfoTrainingItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ActivityInfoTrainingItem,
            newItem: ActivityInfoTrainingItem
        ): Boolean {
            return oldItem == newItem
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
                9 -> {
                    "${DataHelper.formatDuration(item.number.toDouble().toLong())} ${item.unit}"
                }
                else -> {
                    item.getUnitInf()
                }
            }
            tvDescription.text = item.description
            ivIcon.setImageResource(item.img)
        }
    }
}