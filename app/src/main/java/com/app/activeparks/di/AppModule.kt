package com.app.activeparks.di

import android.content.Context
import android.content.SharedPreferences
import com.app.activeparks.data.repository.Repository
import com.app.activeparks.data.storage.Preferences
import com.app.activeparks.ui.event.util.EventController
import com.app.activeparks.util.SESSION_REPOSITORY
import com.polidea.rxandroidble2.RxBleClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Created by O.Dziuba on 17.11.2023.
 */

val appModule = module {
    single { RxBleClient.create(androidContext()) }
    single<SharedPreferences> {
        androidContext().getSharedPreferences(SESSION_REPOSITORY, Context.MODE_PRIVATE)
    }
    single { Preferences(androidContext()) }
    single { EventController(androidContext()) }
    single { Repository(get()) }
}