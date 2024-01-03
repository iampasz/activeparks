package com.app.activeparks.ui.active.util

import android.annotation.SuppressLint
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.app.activeparks.ui.active.model.ActivityState

class BluetoothService : Service() {

    private  lateinit var bluetoothLeScanner: BluetoothLeScanner
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var device: BluetoothDevice? = null

    private var activeState: ActivityState? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    inner class LocalBinder : Binder() {
        fun getService(): BluetoothService = this@BluetoothService
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(bluetoothGattCallback: BluetoothGattCallback) {
        device?.connectGatt(this@BluetoothService, false, bluetoothGattCallback)
    }

    override fun onBind(intent: Intent?): IBinder {
        return LocalBinder()
    }

    @SuppressLint("MissingPermission")
    fun scanBluetoothDevises(scanCallBack: ScanCallback) {
        bluetoothManager =
            this@BluetoothService.getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        bluetoothLeScanner.startScan(scanCallBack)
    }

    @SuppressLint("MissingPermission")
    fun stopScanBluetoothDevises(scanCallBack: ScanCallback) {
        bluetoothLeScanner.stopScan(scanCallBack)
    }

    fun setDevise(device: BluetoothDevice) {
        this.device = device
    }

    fun setActiveState(activeState: ActivityState) {
        this.activeState = activeState
    }

    fun getGetActiveState(): ActivityState? {
        return activeState
    }

}