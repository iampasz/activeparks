package com.app.activeparks.di

import com.app.activeparks.ui.active.ActiveViewModel
import com.app.activeparks.ui.active.fragments.pulseGadget.PulseGadgetViewModel
import com.app.activeparks.ui.active.fragments.saveActivity.SaveActivityViewModel
import com.app.activeparks.ui.event.viewmodel.EventRouteViewModel
import com.app.activeparks.ui.profile.ProfileViewModel
import com.app.activeparks.ui.registration.RegistrationViewModel
import com.app.activeparks.ui.registration.fragments.forgotPassword.ForgotPasswordViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ActiveViewModel(get(), get(), get(), get()) }
    viewModel { SaveActivityViewModel(get()) }
    viewModel { PulseGadgetViewModel() }
    viewModel { EventRouteViewModel(get()) }
    viewModel { ProfileViewModel(get()) }

    viewModel { ForgotPasswordViewModel(get()) }
    viewModel { RegistrationViewModel(get(), androidContext()) }
}