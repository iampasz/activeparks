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
import com.technodreams.activeparks.databinding.ItemPagerPhotoGalleryBinding

class GalleryPagerAdapter(
    private val resources: Resources
) : RecyclerView.Adapter<GalleryPagerAdapter.LevelOfActivityVH>() {

    class LevelOfActivityVH(binding: ItemPagerPhotoGalleryBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val callback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelOfActivityVH {
        return LevelOfActivityVH(
            ItemPagerPhotoGalleryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.currentList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LevelOfActivityVH, position: Int) {
        val item = list.currentList[position]

        ItemPagerPhotoGalleryBinding.bind(holder.itemView).apply {
            Glide.with(photo.context)
                .load(item)
                .error(R.drawable.ic_prew)
                .into(photo)
        }
    }
}