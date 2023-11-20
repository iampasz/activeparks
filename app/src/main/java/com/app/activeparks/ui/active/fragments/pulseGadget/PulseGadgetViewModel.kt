package com.app.activeparks.ui.active.fragments.pulseGadget

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polidea.rxandroidble2.RxBleDevice
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * Created by O.Dziuba on 17.11.2023.
 */
class PulseGadgetViewModel() : ViewModel() {

    @SuppressLint("CheckResult")
    fun connectToMiBand(miBandDevice: RxBleDevice) {
        viewModelScope.launch {
            miBandDevice.apply {
                establishConnection(false).flatMap {
                    it.setupNotification(UUID.fromString(""))
                }
                    .doOnNext {
                        it.subscribe {
                            val s = ""
                            val ss = ""
                        }

                    }
                    .subscribe(
                        {
                        }, {

                        }
                    )

            }


        }
    }
}