package com.app.activeparks.di

import android.content.Context
import android.content.SharedPreferences
import com.polidea.rxandroidble2.RxBleClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Created by O.Dziuba on 17.11.2023.
 */

val appModule = module {
    single { RxBleClient.create(androidContext()) }
    single<SharedPreferences> {
        androidContext().getSharedPreferences("com.app.activeparks", Context.MODE_PRIVATE)
    }
}