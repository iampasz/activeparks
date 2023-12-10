package com.app.activeparks.di

import com.app.activeparks.data.repository.ctiveState.ActivityStateRepository
import com.app.activeparks.data.repository.ctiveState.ActivityStateRepositoryImpl
import com.app.activeparks.data.repository.eventState.EventStateRepository
import com.app.activeparks.data.repository.eventState.EventStateRepositoryImpl
import com.app.activeparks.data.repository.profileState.ProfileStateRepository
import com.app.activeparks.data.repository.profileState.ProfileStateRepositoryImpl
import com.app.activeparks.data.repository.saveActivity.SaveActivityRepository
import com.app.activeparks.data.repository.saveActivity.SaveActivityRepositoryImpl
import org.koin.dsl.module

/**
 * Created by O.Dziuba on 07.11.2023.
 */

val repositoryModule = module{
        single<SaveActivityRepository> { SaveActivityRepositoryImpl(get()) }
        single<ActivityStateRepository> { ActivityStateRepositoryImpl(get()) }

        single<EventStateRepository> { EventStateRepositoryImpl(get()) }
        single<ProfileStateRepository> { ProfileStateRepositoryImpl(get()) }
}