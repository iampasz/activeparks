package com.app.activeparks.di

import androidx.lifecycle.ViewModelProvider
import com.app.activeparks.ui.active.ActiveViewModel
import com.app.activeparks.ui.active.fragments.pulseGadget.PulseGadgetViewModel
import com.app.activeparks.ui.active.fragments.saveActivity.SaveActivityViewModel
import com.app.activeparks.ui.event.viewmodel.EventRouteViewModel
import com.app.activeparks.ui.event.viewmodel.EventViewModel
import com.app.activeparks.ui.homeWithUser.fragments.blog.HomeBlogViewModel
import com.app.activeparks.ui.homeWithUser.fragments.clubs.HomeClubsViewModel
import com.app.activeparks.ui.homeWithUser.fragments.home.HomeEventsViewModel
import com.app.activeparks.ui.homeWithUser.fragments.location.HomeLocationViewModel
import com.app.activeparks.ui.profile.ProfileViewModel
import com.app.activeparks.ui.registration.RegistrationViewModel
import com.app.activeparks.ui.registration.fragments.forgotPassword.ForgotPasswordViewModel
import com.app.activeparks.ui.selectvideo.SelectVideoViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ActiveViewModel(get(), get(), get(), get(), get()) }
    viewModel { SaveActivityViewModel(get()) }
    viewModel { PulseGadgetViewModel() }
    viewModel { EventRouteViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { SelectVideoViewModel(get()) }

    viewModel { ForgotPasswordViewModel(get()) }
    viewModel { HomeBlogViewModel(get()) }
    viewModel { HomeLocationViewModel(get()) }
    viewModel { HomeClubsViewModel(get(), get()) }
    viewModel { HomeEventsViewModel(get(), get()) }
    viewModel { RegistrationViewModel(get(), androidContext()) }

    viewModel { EventViewModel(get())}
}