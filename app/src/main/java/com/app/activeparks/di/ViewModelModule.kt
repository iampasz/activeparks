package com.app.activeparks.di

import com.app.activeparks.ui.active.ActiveViewModel
import com.app.activeparks.ui.active.fragments.saveActivity.SaveActivityViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ActiveViewModel(get()) }
    viewModel { SaveActivityViewModel(get()) }
}