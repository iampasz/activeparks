package com.app.activeparks.ui.event.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.ui.event.ItemPhotoGallery
import com.app.activeparks.util.extention.DataHelper
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.visible
import com.squareup.picasso.Picasso
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ItemEventsBinding
import com.technodreams.activeparks.databinding.ItemPhotoGalleryBinding


/**
 * Created by O.Dziuba on 18.12.2023.
 */
class GalleryAdapter(
    private val event: (ItemPhotoGallery) -> Unit
) : RecyclerView.Adapter<GalleryAdapter.LevelOfActivityVH>() {

    class LevelOfActivityVH(binding: ItemPhotoGalleryBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val callback = object : DiffUtil.ItemCallback<ItemPhotoGallery>() {
        override fun areItemsTheSame(oldItem: ItemPhotoGallery, newItem: ItemPhotoGallery): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ItemPhotoGallery,
            newItem: ItemPhotoGallery
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
        ItemEventsBinding.bind(holder.itemView).apply {
            val context = tvTitle.context

            item.imageUrl.let {
                Picasso.get().load(item.imageUrl).into(photo)
            }

            photo.setOnClickListener {
                event(item)
            }

            tvTitle.text = if (DataHelper.isEventInThePast(item.startsAt)) {
                context.getString(R.string.tv_event_past)
            } else {
                context.getString(R.string.tv_event_new)
            }

            if (item.countUser > 1) {
                gCounter.visible()
                circleAuthor.gone()

                counterText.apply {
                    text = "+${(item.countUser - 1)}"
                }
                item.imageUrl.let {
                    Picasso.get().load(item.createdBy.photo).into(imageFirst)
                }
            } else {
                gCounter.gone()
                item.imageUrl.let {
                    Picasso.get().load(item.createdBy.photo).into(imageAuthor)
                }
            }

            tvDescription.text = item.shortDescription
            tvStartPoint.text = item.startAdressPoint

            date.text = DataHelper.formatDateTimeRange(item.startsAt, item.finishesAt)
        }
    }
}