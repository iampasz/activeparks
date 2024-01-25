package com.app.activeparks.ui.userProfile.activities

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.data.model.activity.ActivityItemResponse
import com.app.activeparks.ui.active.model.TypeOfActivity
import com.app.activeparks.ui.userProfile.model.ActivityItemType
import com.app.activeparks.util.extention.toInfo
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ItemActivityDataHearedBinding
import com.technodreams.activeparks.databinding.ItemActivityListBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 * Created by O.Dziuba on 17.01.2024.
 */
class ActivityAdapterWIthHeader(
    val item: (ActivityItemResponse) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: MutableList<ActivityItemType> = mutableListOf()

    companion object {
        private const val HEADER_TYPE = 0
        private const val ITEM_TYPE = 1
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<ActivityItemType>) {
        items.clear()
        items.addAll(data)

        notifyDataSetChanged()
    }

    private fun getYear(timeInMillis: Long): String {
        val sdf = SimpleDateFormat("yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        return sdf.format(calendar.time)
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ActivityItemType.Header -> HEADER_TYPE
            is ActivityItemType.Item -> ITEM_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            HEADER_TYPE -> {
                val binding = ItemActivityDataHearedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                HeaderViewHolder(binding)
            }

            ITEM_TYPE -> {
                val binding = ItemActivityListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ItemViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is ActivityItemType.Header -> {
                val headerHolder = holder as HeaderViewHolder
                headerHolder.bind(item.year)
            }

            is ActivityItemType.Item -> {
                val itemHolder = holder as ItemViewHolder
                itemHolder.bind(item.activity)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class HeaderViewHolder(private val binding: ItemActivityDataHearedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(year: String) {
            binding.tvTitle.text = year
        }
    }

    inner class ItemViewHolder(private val binding: ItemActivityListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ActivityItemResponse) {
            with(binding) {
                ivActivity.setOnClickListener {
                    item(item)
                }

                tvActivityTitle.text = item.title
                tvDistanceValue.text =
                    tvDistanceValue.context.getString(R.string.tv_km, item.distance)
                tvCaloriesValue.text = tvCaloriesValue.context.getString(
                    R.string.tv_kKal,
                    item.calories.toDouble().toInfo()
                )
                tvDate.text = formatDateActivity(item.startsAt * 1000)
                tvTimeValue.text = formatDurationActivity(item.finishesAt - item.startsAt)

                tvActivityTitle.setCompoundDrawablesWithIntrinsicBounds(
                    TypeOfActivity.getTypeOfActivity()[item.activityType.toInt()].img,
                    0,
                    0,
                    0
                )


                if (item.coverImage.isEmpty() || item.coverImage == "null") {
                    ivActivity.setImageResource(R.drawable.activity_back)
                } else {
                    Glide.with(ivActivity.context)
                        .load(item.coverImage)
                        .into(ivActivity)
                }
            }
        }


        private fun formatDurationActivity(durationInMillis: Long): String {
            val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = durationInMillis

            return dateFormat.format(calendar.time)
        }

        private fun formatDateActivity(timeInMillis: Long): String {
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeInMillis
            return sdf.format(calendar.time)
        }
    }
}

