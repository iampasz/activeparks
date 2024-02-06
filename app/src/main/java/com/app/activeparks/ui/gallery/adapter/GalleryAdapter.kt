package com.app.activeparks.ui.gallery.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.data.model.gallery.PhotoItem
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ItemPhotoGalleryBinding

class GalleryAdapter(
    private val photoItem: (Int) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.LevelOfActivityVH>() {

    class LevelOfActivityVH(binding: ItemPhotoGalleryBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val callback = object : DiffUtil.ItemCallback<PhotoItem>() {
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PhotoItem,
            newItem: PhotoItem
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    val list = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelOfActivityVH {
        return LevelOfActivityVH(
            ItemPhotoGalleryBinding.inflate(
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

        holder.itemView.setOnClickListener{
            photoItem(position)
        }

        ItemPhotoGalleryBinding.bind(holder.itemView).apply {
            item?.url?.let {
                Glide
                    .with(photo.context)
                    .load(item.url)
                    .error(R.drawable.ic_prew)
                    .into(photo) }
        }
    }
}