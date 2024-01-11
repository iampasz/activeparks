package com.app.activeparks.ui.homeWithUser.fragments.blog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.data.model.news.ItemNews
import com.bumptech.glide.Glide
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ItemHomeBlogListBinding
import java.text.SimpleDateFormat
import java.util.Locale


/**
 * Created by O.Dziuba on 18.12.2023.
 */
class HomeNewsAdapter(
    private val news: (ItemNews) -> Unit
) : RecyclerView.Adapter<HomeNewsAdapter.LevelOfActivityVH>() {

    class LevelOfActivityVH(binding: ItemHomeBlogListBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val callback = object : DiffUtil.ItemCallback<ItemNews>() {
        override fun areItemsTheSame(oldItem: ItemNews, newItem: ItemNews): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ItemNews,
            newItem: ItemNews
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    val list = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelOfActivityVH {
        return LevelOfActivityVH(
            ItemHomeBlogListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.currentList.size

    override fun onBindViewHolder(holder: LevelOfActivityVH, position: Int) {
        val item = list.currentList[position]
        ItemHomeBlogListBinding.bind(holder.itemView).apply {
            tvDescription.text = item.title

            setDate(item)

            Glide
                .with(ivNews.context)
                .load(item.imageUrl)
                .error(R.drawable.ic_prew)
                .into(ivNews)

            ivNews.setOnClickListener {
                news(item)
            }
        }
    }

    private fun ItemHomeBlogListBinding.setDate(item: ItemNews) {
        val serverData = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = serverData.parse(item.createdAt)

        val timestamp = date?.time ?: 0L

        val dateFormat = SimpleDateFormat("dd/MM/yy | HH:mm", Locale.getDefault())
        val formattedDateTime = dateFormat.format(timestamp)
        tvTime.text = formattedDateTime
    }
}