package com.app.activeparks.di

import com.app.activeparks.data.repository.ctiveState.ActivityStateRepository
import com.app.activeparks.data.repository.ctiveState.ActivityStateRepositoryImpl
import com.app.activeparks.data.repository.eventState.EventStateRepository
import com.app.activeparks.data.repository.eventState.EventStateRepositoryImpl
import com.app.activeparks.data.repository.profileState.ProfileStateRepository
import com.app.activeparks.data.repository.profileState.ProfileStateRepositoryImpl
import com.app.activeparks.data.repository.registratio.UserRepository
import com.app.activeparks.data.repository.registratio.UserRepositoryImpl
import com.app.activeparks.data.repository.saveActivity.SaveActivityRepository
import com.app.activeparks.data.repository.saveActivity.SaveActivityRepositoryImpl
import com.app.activeparks.data.repository.sesion.MobileApiSessionRepository
import com.app.activeparks.data.repository.sesion.SharedPreferencesMobileApiSessionRepository
import com.app.activeparks.data.repository.weather.WeatherRepository
import com.app.activeparks.data.repository.weather.WeatherRepositoryImpl
import org.koin.dsl.module

/**
 * Created by O.Dziuba on 07.11.2023.
 */

val repositoryModule = module{
        single<SaveActivityRepository> { SaveActivityRepositoryImpl(get(), get()) }
        single<ActivityStateRepository> { ActivityStateRepositoryImpl(get()) }

        single<EventStateRepository> { EventStateRepositoryImpl(get()) }
        single<ProfileStateRepository> { ProfileStateRepositoryImpl(get()) }
        single<WeatherRepository> { WeatherRepositoryImpl(get()) }
        single<MobileApiSessionRepository> { SharedPreferencesMobileApiSessionRepository(get()) }
        single<UserRepository> { UserRepositoryImpl(get(), get()) }
}