package com.app.activeparks.ui.userProfile.statisticFragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.data.model.activity.ActivityItemResponse
import com.app.activeparks.ui.active.model.TypeOfActivity
import com.app.activeparks.util.extention.DataHelper
import com.app.activeparks.util.extention.toInfo
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ItemActivityListBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


/**
 * Created by O.Dziuba on 25.12.2023.
 */
class ActivityAdapter(
    val item: (ActivityItemResponse) -> Unit
) : RecyclerView.Adapter<ActivityAdapter.ActivityVH>() {

    class ActivityVH(binding: ItemActivityListBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val callback = object : DiffUtil.ItemCallback<ActivityItemResponse>() {
        override fun areItemsTheSame(
            oldItem: ActivityItemResponse,
            newItem: ActivityItemResponse
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ActivityItemResponse,
            newItem: ActivityItemResponse
        ): Boolean {
            return false
        }
    }

    val list = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityVH {
        return ActivityVH(
            ItemActivityListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.currentList.size

    override fun onBindViewHolder(holder: ActivityVH, position: Int) {
        val item = list.currentList[position]
        ItemActivityListBinding.bind(holder.itemView).apply {
            ivActivity.setOnClickListener {
                item(item)
            }

            tvActivityTitle.text = item.title
            tvDistanceValue.text = tvDistanceValue.context.getString(R.string.tv_km, item.distance)
            tvCaloriesValue.text = tvCaloriesValue.context.getString(
                R.string.tv_kKal,
                item.calories.toDouble().toInfo()
            )
            tvDate.text = DataHelper.formatDateActivity(item.startsAt * 1000)
            tvTimeValue.text = DataHelper.formatDurationActivity(item.finishesAt - item.startsAt)

            try {
                tvActivityTitle.setCompoundDrawablesWithIntrinsicBounds(
                    TypeOfActivity.getTypeOfActivity()[item.activityType.toInt()].img,
                    0,
                    0,
                    0
                )
            } catch (_: Exception) {}


            if (item.coverImage.isEmpty() || item.coverImage == "null") {
                ivActivity.setImageResource(R.drawable.activity_back)
            } else {
                Glide.with(ivActivity.context)
                    .load(item.coverImage)
                    .into(ivActivity)
            }
        }
    }
}