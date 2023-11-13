package com.app.activeparks

import android.app.Application
import com.app.activeparks.di.dbModule
import com.app.activeparks.di.repositoryModule
import com.app.activeparks.di.useCaseModule
import com.app.activeparks.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
    }

    private fun initKoin() {
        startKoin {
            module {
                androidContext(this@App)
                modules(
                    listOf(
                        dbModule,
                        viewModelModule,
                        repositoryModule,
                        useCaseModule
                    )
                )
            }
        }
    }
}