package com.app.activeparks.di

import com.polidea.rxandroidble2.RxBleClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Created by O.Dziuba on 17.11.2023.
 */

val appModule = module {
    single { RxBleClient.create(androidContext()) }
}