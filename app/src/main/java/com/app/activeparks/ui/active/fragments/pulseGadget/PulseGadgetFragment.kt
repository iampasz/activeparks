package com.app.activeparks.ui.active.fragments.pulseGadget

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.activeparks.ui.active.ActiveViewModel
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.visible
import com.technodreams.activeparks.databinding.FragmentPulseGadgetBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * Created by O.Dziuba on 06.11.2023.
 */
class PulseGadgetFragment : Fragment() {

    lateinit var binding: FragmentPulseGadgetBinding
    private val viewModel: ActiveViewModel by activityViewModel()

    private val bluetoothDevices = mutableListOf<BluetoothDevice>()
    private var bluetoothLeScanner: BluetoothLeScanner? = null

    private lateinit var bluetoothAdapter: BluetoothAdapter
    val adapter = BleDeviceAdapter {}

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                gatt?.discoverServices()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {

        }

        @Deprecated("Deprecated in Java")
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {

        }
    }

    private val bleCallBack = object : ScanCallback() {
        @SuppressLint("NotifyDataSetChanged")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            val device = result.device
            //TODO for test
            if (device.name == "Mi Smart Band 5") {
                if (ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                device.connectGatt(requireContext(), false, gattCallback)
            }

            if (!bluetoothDevices.contains(device) && !device.name.isNullOrEmpty()) {
                bluetoothDevices.add(device)

                adapter.list.submitList(bluetoothDevices)
                adapter.notifyDataSetChanged()

                if (ActivityCompat.checkSelfPermission(
                        requireActivity(),
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                if (!device.name.isNullOrEmpty()) Log.e(
                    "!@#!@#",
                    "onScanResult: ${device.name}    ${device.uuids}"
                )

                val scanRecord = result.scanRecord
                val serviceUuids = scanRecord?.serviceUuids
                if (serviceUuids != null) {
                    for (uuid in serviceUuids) {
                        if (!device.name.isNullOrEmpty()) Log.e(
                            "!@#!@#",
                            "onScanResult: ${device.name}    ${device.uuids}"
                        )
                    }
                }
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPulseGadgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        findBLEGadget()
        binding.rvPulseGadget.adapter = adapter
    }

    private var requestBluetooth =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                //granted
                startScanning()
            } else {
                //deny
            }
        }

    private fun findBLEGadget() {
        val bluetoothManager = requireContext().getSystemService(BluetoothManager::class.java)
        bluetoothAdapter = bluetoothManager.adapter

        if (!requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.BLUETOOTH),
                PERMISSION_REQUEST_CODE
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            )
            startScanning()
        } else {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestBluetooth.launch(enableBtIntent)
            startScanning()
        }
    }

    private val requestMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.BLUETOOTH_SCAN, false) -> {
                startScanning()
            }
            else -> { }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initListener() {
        with(binding) {
            ivBack.setOnClickListener { requireActivity().onBackPressed() }
            swPG.apply {
                isChecked = viewModel.activityState.isPulseGadgetConnected
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.activityState.isPulseGadgetConnected = isChecked
                }
            }

            srUpdate.setOnRefreshListener {
                tvUpdate.gone()
                gProgress.visible()
                adapter.list.submitList(listOf())
                adapter.notifyDataSetChanged()
                srUpdate.isRefreshing = false
                bluetoothDevices.clear()
                startScanning()
            }
        }
    }

    private fun startScanning() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

        bluetoothLeScanner?.startScan(bleCallBack)
        binding.gProgress.visible()
        Handler(Looper.getMainLooper()).postDelayed({
            bluetoothLeScanner?.stopScan(bleCallBack)

            with(binding) {
                tvUpdate.visible()
                gProgress.gone()
            }
        }, TIME_FOR_SEARCH)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        bluetoothLeScanner?.stopScan(bleCallBack)
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 2
        private const val TIME_FOR_SEARCH = 10000L
    }
}