package com.app.activeparks.ui.active.adapte

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
            return false
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
            tvTitle.text = item.title.replace("<br>", " ")
            tvTitle.setOnClickListener {
                list.currentList.forEach { it.isSelected = false }
                list.currentList[position].isSelected = true
                list.submitList(list.currentList)
                notifyDataSetChanged()

                itemSelected(item)
            }

            if (item.isSelected) {
                setSelected(
                    item.img,
                    R.drawable.ic_selected,
                    R.color.white,
                    R.drawable.button_green,
                    R.color.white
                )
            } else {
                setSelected(
                    item.img,
                    0,
                    R.color.background_dialog,
                    R.color.top_panel,
                    R.color.text_color
                )
            }
        }
    }

    private fun ItemActivityInfoBinding.setSelected(
        imgFirst: Int,
        imgSecond: Int,
        colorTint: Int,
        background: Int,
        textColor: Int
    ) {
        tvTitle.apply {
            setCompoundDrawablesWithIntrinsicBounds(
                imgFirst,
                0,
                imgSecond,
                0
            )
            setTint(colorTint)
            setBackgroundResource(background)
            setTextColor(ContextCompat.getColor(context, textColor))
        }
    }
}