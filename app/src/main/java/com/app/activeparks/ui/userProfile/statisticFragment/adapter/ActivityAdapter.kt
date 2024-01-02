package com.app.activeparks.ui.userProfile.statisticFragment.adapter

import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.data.db.entity.ActiveEntity
import com.app.activeparks.ui.active.model.TypeOfActivity
import com.app.activeparks.util.extention.toInfo
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
    val item: (ActiveEntity) -> Unit
) : RecyclerView.Adapter<ActivityAdapter.ActivityVH>() {

    class ActivityVH(binding: ItemActivityListBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val callback = object : DiffUtil.ItemCallback<ActiveEntity>() {
        override fun areItemsTheSame(oldItem: ActiveEntity, newItem: ActiveEntity): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ActiveEntity,
            newItem: ActiveEntity
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
            tvDate.text = formatDateActivity(item.startsAt)
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
//                val uri = Uri.parse(item.coverImage)
//
//                val f = uri.path?.let { File(it) }
//
//                Glide.with(ivActivity.context)
//                    .load(f)
//                    .into(ivActivity)

//                val uri = Uri.parse(item.coverImage)
//                val file = File(uri.path)

//                Glide.with(ivActivity.context)
//                    .load("content://com.miui.gallery.open/raw/%2Fstorage%2Femulated%2F0%2FPictures%2FViber%2FIMG-abc742afc187ad6a60e457f9a173237c-V.jpg")
//                    .into(ivActivity)
//

                val selectedImageUri: Uri = Uri.parse(item.coverImage)

                try {
                    // Отримати Bitmap з Uri
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        ivActivity.context.contentResolver,
                        selectedImageUri
                    )

                    // Встановити Bitmap в ImageView
                    ivActivity.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
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