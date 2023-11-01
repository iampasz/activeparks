package com.app.activeparks.ui.active.adapte

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.ui.active.model.TypeOfActivity
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.setTint
import com.app.activeparks.util.extention.visible
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ItemActivityTypeBinding


/**
 * Created by O.Dziuba on 30.10.2023.
 */
class ActivityHomeTypeAdapter(
    private val itemSelected: (TypeOfActivity) -> Unit
) : RecyclerView.Adapter<ActivityHomeTypeAdapter.TypeOfActivityVH>() {

    class TypeOfActivityVH(binding: ItemActivityTypeBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val callback = object : DiffUtil.ItemCallback<TypeOfActivity>() {
        override fun areItemsTheSame(
            oldItem: TypeOfActivity,
            newItem: TypeOfActivity
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: TypeOfActivity,
            newItem: TypeOfActivity
        ): Boolean {
            return oldItem.id == newItem.id
        }

    }

    val list = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeOfActivityVH {
        return TypeOfActivityVH(
            ItemActivityTypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.currentList.size

    override fun onBindViewHolder(holder: TypeOfActivityVH, position: Int) {
        val item = list.currentList[position]
        ItemActivityTypeBinding.bind(holder.itemView).apply {
            tvTitle.text = item.title
            tvTitle.setOnClickListener {
                itemSelected(item)
            }

            if (item.isSelected) {
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
                tvTitle.setBackgroundResource(R.drawable.backround_edit_text)
            }

//            item.description?.let {
//                tvDescription.apply {
//                    text = it
//                    visible()
//                }
//            } ?: { tvDescription.gone() }
        }
    }
}