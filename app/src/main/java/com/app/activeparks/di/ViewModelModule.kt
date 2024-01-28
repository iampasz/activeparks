package com.app.activeparks.di

import com.app.activeparks.ui.active.ActiveViewModel
import com.app.activeparks.ui.active.fragments.pulseGadget.PulseGadgetViewModel
import com.app.activeparks.ui.active.fragments.saveActivity.SaveActivityViewModel
import com.app.activeparks.ui.event.viewmodel.EventRouteViewModel
import com.app.activeparks.ui.event.viewmodel.EventViewModel
import com.app.activeparks.ui.homeWithUser.fragments.blog.HomeBlogViewModel
import com.app.activeparks.ui.homeWithUser.fragments.clubs.HomeClubsViewModel
import com.app.activeparks.ui.homeWithUser.fragments.event.HomeEventsViewModel
import com.app.activeparks.ui.homeWithUser.fragments.location.HomeLocationViewModel
import com.app.activeparks.ui.profile.ProfileViewModel
import com.app.activeparks.ui.registration.RegistrationViewModel
import com.app.activeparks.ui.registration.fragments.forgotPassword.ForgotPasswordViewModel
import com.app.activeparks.ui.userProfile.statisticFragment.StatisticViewModel
import com.app.activeparks.ui.selectvideo.SelectVideoViewModel
import com.app.activeparks.MainViewModel
import com.app.activeparks.ui.clubs.ClubsViewModel
import com.app.activeparks.ui.clubs.ClubsViewModelKT
import com.app.activeparks.ui.event.fragments.MainEventViewModel
import com.app.activeparks.ui.gallery.GalleryViewModel
import com.app.activeparks.ui.homeWithUser.fragments.home.MainHomeViewModel
import com.app.activeparks.ui.participants.ParticipantsViewModel
import com.app.activeparks.ui.userProfile.edit.EditProfileViewModel
import com.app.activeparks.ui.userProfile.home.ProfileHomeViewModel
import com.app.activeparks.ui.userProfile.menu.MenuProfileViewModel
import com.app.activeparks.ui.userProfile.info.UserInfoViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ActiveViewModel(get(), get(), get(), get(), get()) }
    viewModel { SaveActivityViewModel(get(), get()) }
    viewModel { StatisticViewModel(get(), get()) }
    viewModel { PulseGadgetViewModel() }
    viewModel { MenuProfileViewModel(get(), get(), get(), get()) }
    viewModel { UserInfoViewModel(get(), get(), get()) }
    viewModel { ProfileHomeViewModel(get(), get(), get()) }
    viewModel { EditProfileViewModel(get(), get()) }
    viewModel { MainHomeViewModel(get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel { EventRouteViewModel(get(),get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { SelectVideoViewModel(get()) }

    viewModel { ForgotPasswordViewModel(get()) }
    viewModel { HomeBlogViewModel(get()) }
    viewModel { HomeLocationViewModel(get()) }
    viewModel { HomeClubsViewModel(get(), get()) }
    viewModel { HomeEventsViewModel(get(), get()) }
    viewModel { RegistrationViewModel(get(), androidContext()) }

    viewModel { EventViewModel(get()) }
    viewModel { ParticipantsViewModel(get()) }
    viewModel { MainEventViewModel(get(),get(),get()) }
    viewModel { ClubsViewModelKT(get(), get()) }
    viewModel { ClubsViewModel(get()) }
    viewModel { MainEventViewModel(get(), get(), get()) }
    viewModel { GalleryViewModel() }

}