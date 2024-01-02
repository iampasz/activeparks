package com.app.activeparks.ui.userProfile.video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.ui.userProfile.model.VideoItem
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ItemUserProfileVideoBinding

/**
 * Created by O.Dziuba on 22.12.2023.
 */
class VideoItemAdapter : RecyclerView.Adapter<VideoItemAdapter.VideoItemVH>() {

    class VideoItemVH(binding: ItemUserProfileVideoBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val callback = object : DiffUtil.ItemCallback<VideoItem>() {
        override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: VideoItem,
            newItem: VideoItem
        ): Boolean {
            return false
        }
    }

    val list = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoItemVH {
        return VideoItemVH(
            ItemUserProfileVideoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.currentList.size

    override fun onBindViewHolder(holder: VideoItemVH, position: Int) {
        val item = list.currentList[position]
        ItemUserProfileVideoBinding.bind(holder.itemView).apply {
            tvDescription.text = item.description
            vState.setState(item.videoState)
            Glide.with(ivImg.context).load(item.img).error(R.drawable.ic_prew)
                .into(ivImg)

        }
    }
}