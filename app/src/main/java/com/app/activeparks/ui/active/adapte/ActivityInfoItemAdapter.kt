package com.app.activeparks.ui.active.adapte

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.ui.active.model.ActivityInfoItem
import com.app.activeparks.util.extention.setTint
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ItemActivityInfoBinding


/**
 * Created by O.Dziuba on 30.10.2023.
 */
class ActivityInfoItemAdapter(
    private val id: Int,
    private val itemSelected: (ActivityInfoItem) -> Unit
) : RecyclerView.Adapter<ActivityInfoItemAdapter.ActivityInfoItemVH>() {

    class ActivityInfoItemVH(binding: ItemActivityInfoBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val callback = object : DiffUtil.ItemCallback<ActivityInfoItem>() {
        override fun areItemsTheSame(
            oldItem: ActivityInfoItem,
            newItem: ActivityInfoItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ActivityInfoItem,
            newItem: ActivityInfoItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }

    val list = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityInfoItemVH {
        return ActivityInfoItemVH(
            ItemActivityInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.currentList.size

    override fun onBindViewHolder(holder: ActivityInfoItemVH, position: Int) {
        val item = list.currentList[position]
        ItemActivityInfoBinding.bind(holder.itemView).apply {
            tvTitle.text = item.title
            tvTitle.setOnClickListener {
                itemSelected(item)
            }

            if (item.id == id) {
                tvTitle.setCompoundDrawablesWithIntrinsicBounds(
                    item.img,
                    0,
                    R.drawable.ic_selected,
                    0
                )

                tvTitle.setTint(R.color.white)
                tvTitle.setBackgroundResource(R.drawable.button_green)
                tvTitle.setTextColor(ContextCompat.getColor(tvTitle.context, R.color.white))
            } else {
                tvTitle.setCompoundDrawablesWithIntrinsicBounds(
                    item.img,
                    0,
                    0,
                    0
                )
                tvTitle.setTint(R.color.background_dialog)
                tvTitle.setBackgroundResource(R.color.top_panel)
            }
        }
    }
}