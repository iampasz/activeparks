package com.app.activeparks.ui.event.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.activeparks.data.model.sportevents.ItemEvent
import com.app.activeparks.ui.event.interfaces.OnItemClickListener
import com.app.activeparks.util.extention.gone
import com.squareup.picasso.Picasso
import com.technodreams.activeparks.databinding.ItemEventBinding
import com.technodreams.activeparks.databinding.ItemEventCalendarBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EventsListAdapterKT(private val listener : OnItemClickListener) : RecyclerView.Adapter<EventsListAdapterKT.ViewHolder>(){
    private lateinit var binding: ItemEventCalendarBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding= ItemEventCalendarBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(differ.currentList[position])
        holder.setIsRecyclable(false)

        binding.calendarInclude.photo.setOnClickListener{
            listener.onItemClick(position)
        }
    }

    override fun getItemCount()=differ.currentList.size

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun setData(item : ItemEvent){
            item.imageUrl?.let {
                Picasso.get().load(item.imageUrl).into(binding.calendarInclude.photo)
            }

            binding.calendarInclude.date.text = item.updatedAt
            binding.calendarInclude.nameEvent.text = item.title
            binding.calendarInclude.statusEvent.text = ""
            binding.calendarInclude.textView4.text = ""
            binding.calendarInclude.textView5.text = ""
            binding.calendarInclude.circleImageFirst.gone()
            binding.calendarInclude.circleImageSecond.gone()
            binding.calendarInclude.date.text = changeDataFormat(item.startsAt)


            item.memberAmount?.let {
                binding.calendarInclude.counterText.text = "+${item.memberAmount -1}"
            }

        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<ItemEvent>(){
        override fun areItemsTheSame(oldItem: ItemEvent, newItem: ItemEvent): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ItemEvent, newItem: ItemEvent): Boolean {
            //return oldItem == newItem
            return oldItem.id == newItem.id
        }
    }

    val differ = AsyncListDiffer(this,differCallback)


    private fun changeDataFormat(dateString:String) : String{
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(dateString, inputFormatter)
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yy | HH:mm")
        return dateTime.format(outputFormatter)
    }
}