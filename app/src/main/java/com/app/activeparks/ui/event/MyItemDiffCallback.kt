package com.app.activeparks.ui.event


import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil

class MyItemDiffCallback() : DiffUtil.ItemCallback<TextView>() {
    override fun areItemsTheSame(oldItem: TextView, newItem: TextView): Boolean {
        return oldItem.text == newItem.text // Перевірка чи елементи є однаковими за порядковим номером
    }

    override fun areContentsTheSame(oldItem: TextView, newItem: TextView): Boolean {
        return oldItem.text == newItem.text // Перевірка чи вміст елементів однаковий
    }
}