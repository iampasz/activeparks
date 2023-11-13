package com.app.activeparks.ui.active.fragments.level

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.ui.active.model.ActivityInfoTrainingItem
import com.technodreams.activeparks.databinding.ItemActivityTrainingBinding


/**
 * Created by O.Dziuba on 30.10.2023.
 */
class ActivityInfoTrainingAdapter(
    private val itemSelected: (ActivityInfoTrainingItem) -> Unit
) : RecyclerView.Adapter<ActivityInfoTrainingAdapter.ActivityInfoTrainingItemVH>() {

    class ActivityInfoTrainingItemVH(binding: ItemActivityTrainingBinding) :
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
            return false
        }
    }

    val list = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityInfoTrainingItemVH {
        return ActivityInfoTrainingItemVH(
            ItemActivityTrainingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.currentList.size

    override fun onBindViewHolder(holder: ActivityInfoTrainingItemVH, position: Int) {
        val item = list.currentList[position]
        ItemActivityTrainingBinding.bind(holder.itemView).apply {
            tvTitle.text = item.title
            tvDescription.text = item.description
            ivIcon.setImageResource(item.img)
        }
    }
}