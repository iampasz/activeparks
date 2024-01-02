package com.app.activeparks.ui.active.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class BluetoothHelper {
    companion object {
        @SuppressLint("MissingPermission")
        fun sortOutDevice(result: ScanResult): BluetoothDevice? {
            val device = result.device
            if (!device.name.isNullOrEmpty()) {
                val scanRecord = result.scanRecord
                val serviceUuids = scanRecord?.serviceUuids

                serviceUuids?.let {
                    for (uuid in serviceUuids) {
                        return device
                    }
                }
            }
            return null
        }

        fun findBLEGadget(
            activity: Activity,
            requestBluetooth: ActivityResultLauncher<Intent>,
            requestMultiplePermissions: ActivityResultLauncher<Array<String>>
        ): Boolean {

            val PERMISSION_REQUEST_CODE = 2

            if (!activity.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                return false
            }

            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.BLUETOOTH
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.BLUETOOTH),
                    PERMISSION_REQUEST_CODE
                )
            }

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                requestMultiplePermissions.launch(
                    arrayOf(
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_ADMIN
                    )
                )
                true
            } else {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                requestBluetooth.launch(enableBtIntent)
                true
            }
        }
    }
}