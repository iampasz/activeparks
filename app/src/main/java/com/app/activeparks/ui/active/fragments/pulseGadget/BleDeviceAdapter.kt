package com.app.activeparks.ui.active.fragments.pulseGadget

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.technodreams.activeparks.R
import com.technodreams.activeparks.databinding.ItemBleGadgetBinding


/**
 * Created by O.Dziuba on 30.10.2023.
 */
class BleDeviceAdapter(
    private val itemSelected: (BluetoothDevice) -> Unit
) : RecyclerView.Adapter<BleDeviceAdapter.LevelOfActivityVH>() {

    class LevelOfActivityVH(binding: ItemBleGadgetBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val callback = object : DiffUtil.ItemCallback<BluetoothDevice>() {
        override fun areItemsTheSame(oldItem: BluetoothDevice, newItem: BluetoothDevice): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: BluetoothDevice,
            newItem: BluetoothDevice
        ): Boolean {
            return false
        }
    }

    val list = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelOfActivityVH {
        return LevelOfActivityVH(
            ItemBleGadgetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.currentList.size

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: LevelOfActivityVH, position: Int) {
        val item = list.currentList[position]
        ItemBleGadgetBinding.bind(holder.itemView).apply {
            if (ActivityCompat.checkSelfPermission(
                    tvTitle.context,
                    Manifest.permission.BLUETOOTH_ADMIN
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            tvTitle.text = item.name
            tvTitle.setOnClickListener{
                 notifyDataSetChanged()
                itemSelected(item)
                tvTitle.setBackgroundResource(R.drawable.backround_event)
            }
        }
    }
}