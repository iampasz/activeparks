package com.app.activeparks.ui.event.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.data.model.sportevents.ItemEventTitle
import com.technodreams.activeparks.R

class EventTypeAdapter(private val itemList: ArrayList<ItemEventTitle>, private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<EventTypeAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_title_event, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]

        // Встановлюємо дані для кожного елемента
        holder.textView.text = currentItem.name
        holder.itemView.isSelected = currentItem.isSelected
        holder.itemView.setBackgroundResource(currentItem.backgroundResource)

        // Встановлюємо слухача для кліку
        holder.itemView.setOnClickListener {
            onItemClick.invoke(position)
        }
    }

    override fun getItemCount(): Int = itemList.size

    // Внутрішній клас ViewHolder
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.text)
    }

    // Функція для оновлення списку елементів
    fun updateList(newList: List<ItemEventTitle>) {
        val diffResult = DiffUtil.calculateDiff(MyDiffCallback(itemList, newList))
        itemList.clear()
        itemList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }



    // Клас для розрахунку різниці між старим і новим списком
    class MyDiffCallback(private val oldList: List<ItemEventTitle>, private val newList: List<ItemEventTitle>) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}