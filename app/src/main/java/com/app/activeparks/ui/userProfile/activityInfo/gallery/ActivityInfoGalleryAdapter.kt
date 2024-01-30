package com.app.activeparks.ui.userProfile.activityInfo.gallery

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.util.extention.FileHelper
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ItemPhotoGalleryActivityBinding


/**
 * Created by O.Dziuba on 30.10.2023.
 */
class ActivityInfoGalleryAdapter(
    private val resources: Resources,
    private val onClick: (Int) -> Unit
) :
    RecyclerView.Adapter<ActivityInfoGalleryAdapter.ActivityGalleryItemVH>() {

    class ActivityGalleryItemVH(binding: ItemPhotoGalleryActivityBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val callback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }
    }

    val list = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityGalleryItemVH {
        return ActivityGalleryItemVH(
            ItemPhotoGalleryActivityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.currentList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ActivityGalleryItemVH, position: Int) {
        val item = list.currentList[position]
        ItemPhotoGalleryActivityBinding.bind(holder.itemView).apply {

            FileHelper.changeSize(ivPhoto, resources, 3)
            Glide.with(ivPhoto.context)
                .load(item)
                .error(R.drawable.ic_prew)
                .into(ivPhoto)

            ivPhoto.setOnClickListener {
                onClick(position)
            }
        }
    }
}