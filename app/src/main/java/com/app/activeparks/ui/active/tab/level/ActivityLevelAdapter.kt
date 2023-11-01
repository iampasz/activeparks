package com.app.activeparks.ui.active.tab.level

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.ui.active.model.LevelOfActivity
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ItemActivityLevelBinding


/**
 * Created by O.Dziuba on 30.10.2023.
 */
class ActivityLevelAdapter(
    private val itemSelected: (LevelOfActivity) -> Unit
) : RecyclerView.Adapter<ActivityLevelAdapter.LevelOfActivityVH>() {

    class LevelOfActivityVH(binding: ItemActivityLevelBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val callback = object : DiffUtil.ItemCallback<LevelOfActivity>() {
        override fun areItemsTheSame(oldItem: LevelOfActivity, newItem: LevelOfActivity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: LevelOfActivity,
            newItem: LevelOfActivity
        ): Boolean {
            return false
        }
    }

    val list = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelOfActivityVH {
        return LevelOfActivityVH(
            ItemActivityLevelBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.currentList.size

    override fun onBindViewHolder(holder: LevelOfActivityVH, position: Int) {
        val item = list.currentList[position]
        ItemActivityLevelBinding.bind(holder.itemView).apply {
            tvTitle.text = item.title
            tvDescription.text = item.description
            ivIcon.setImageResource(item.img)

            root.setOnClickListener {
                list.currentList.forEach { it.isSelected = false }
                list.currentList[position].isSelected = true
                list.submitList(list.currentList)
                notifyDataSetChanged()
            }

            root.setBackgroundResource(
                if (item.isSelected) {
                    R.drawable.button_style_gren
                } else {
                    R.drawable.backround_edit_text
                }
            )
        }
    }
}