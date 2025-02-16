package com.app.activeparks.ui.active.fragments.pulseGadget

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.app.activeparks.ui.active.ActiveViewModel
import com.app.activeparks.ui.active.ActivityForActivity
import com.app.activeparks.ui.active.util.BluetoothHelper
import com.app.activeparks.ui.active.util.BluetoothService
import com.app.activeparks.util.extention.gone
import com.app.activeparks.util.extention.visible
import com.technodreams.activeparks.databinding.FragmentPulseGadgetBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * Created by O.Dziuba on 06.11.2023.
 */

@Suppress("DEPRECATION")
class PulseGadgetFragment : Fragment() {

    lateinit var binding: FragmentPulseGadgetBinding
    private val viewModel: ActiveViewModel by activityViewModel()
    private val TIME_FOR_SEARCH = 20000L
    private var bluetoothService: BluetoothService? = null
    private var isServiceBound = false

    val adapter = BleDeviceAdapter { item ->
        bluetoothService?.setDevise(item)
        viewModel.heartRateList.clear()
        viewModel.activityState.isPulseGadgetConnected = true
        (activity as ActivityForActivity?)?.disconnectCurrentPulse()
        (activity as ActivityForActivity?)?.connectCurrentPulse()
    }

    private val bluetoothDevices = mutableListOf<BluetoothDevice>()
    private var requestBluetooth: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                initListener()
            }
        }
    private val scanCallBack = object : ScanCallback() {
        @SuppressLint("NotifyDataSetChanged")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)

            val device = BluetoothHelper.sortOutDevice(result)
            if (!bluetoothDevices.contains(device)) {
                device?.let {
                    bluetoothDevices.add(device)
                    adapter.list.submitList(bluetoothDevices)
                    adapter.notifyDataSetChanged()
                    viewModel.activityState.minPulse = 300
                    viewModel.activityState.maxPulse = 0
                    viewModel.activityState.currentPulse = 0
                }
            }
        }
    }
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as BluetoothService.LocalBinder
            bluetoothService = binder.getService()
            isServiceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isServiceBound = false
        }
    }


    private val requestMultiplePermissions: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.BLUETOOTH_SCAN, false) -> {
                   initListener()
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

        bindBluetoothService()

        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        bluetoothAdapter?.let {
            if (bluetoothAdapter.isEnabled) {
                initListener()
            } else {
                Toast.makeText(context, "Помилка підключення до блютуз", Toast.LENGTH_SHORT).show()
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                requestBluetooth.launch(enableBtIntent)
            }
        }

        binding.rvPulseGadget.adapter = adapter

        //TODO START test block
        binding.button5.setOnClickListener {
            val answer = (activity as ActivityForActivity?)?.testDevice()
            binding.button5.text = answer.toString()
            viewModel.activityState.isPulseGadgetConnected = true
        }
    }

    private fun bindBluetoothService() {
        val serviceIntent = Intent(requireContext(), BluetoothService::class.java)
        requireContext().bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindBluetoothService() {
        if (isServiceBound) {
            requireContext().unbindService(serviceConnection)
            isServiceBound = false
        }
    }

    @SuppressLint("SuspiciousIndentation", "NotifyDataSetChanged")
    private fun initListener() {
        with(binding) {
            ivBack.setOnClickListener { requireActivity().onBackPressed() }

            srUpdate.setOnRefreshListener {
                tvUpdate.gone()
                gProgress.visible()
                adapter.list.submitList(listOf())
                adapter.notifyDataSetChanged()
                srUpdate.isRefreshing = false
                scanBluetoothDevises(scanCallBack)
            }
        }
    }

    private fun showScanningProgress() {
        binding.gProgress.visible()
        Handler(Looper.getMainLooper()).postDelayed({
            bluetoothService?.stopScanBluetoothDevises(scanCallBack)
            with(binding) {
                tvUpdate.visible()
                gProgress.gone()
            }
        }, TIME_FOR_SEARCH)
    }

    private fun scanBluetoothDevises(scanCallBack: ScanCallback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            )
            bluetoothDevices.clear()
            bluetoothService?.scanBluetoothDevises(scanCallBack)
            showScanningProgress()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothService?.stopScanBluetoothDevises(scanCallBack)
        unbindBluetoothService()
    }
}