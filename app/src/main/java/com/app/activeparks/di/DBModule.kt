package com.app.activeparks.di

import androidx.room.Room
import com.app.activeparks.data.db.AppDataBase
import com.app.activeparks.data.db.dao.activity.ActiveDao
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Created by O.Dziuba on 09.11.2023.
 */

val dbModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDataBase::class.java,
            "app_data_base.db"
        ).fallbackToDestructiveMigration().build()
    }
    single { get<AppDataBase>().activeDao() }
}