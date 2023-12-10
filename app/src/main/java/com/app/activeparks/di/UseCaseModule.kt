package com.app.activeparks.di

import com.app.activeparks.data.useCase.activeState.ActivityStateUseCase
import com.app.activeparks.data.useCase.activeState.ActivityStateUseCaseImpl
import com.app.activeparks.data.useCase.saveActivity.SaveActivityUseCase
import com.app.activeparks.data.useCase.saveActivity.SaveActivityUseCaseImpl
import com.app.activeparks.data.useCase.eventState.EventStateUseCase
import com.app.activeparks.data.useCase.eventState.EventStateUseCaseImpl
import com.app.activeparks.data.useCase.profileState.ProfileStateUseCase
import com.app.activeparks.data.useCase.profileState.ProfileStateUseCaseImpl
import org.koin.dsl.module

/**
 * Created by O.Dziuba on 09.11.2023.
 */

val useCaseModule = module {
    single<SaveActivityUseCase> { SaveActivityUseCaseImpl(get()) }
    single<ActivityStateUseCase> { ActivityStateUseCaseImpl(get()) }
    single<EventStateUseCase> { EventStateUseCaseImpl(get()) }
    single<ProfileStateUseCase> { ProfileStateUseCaseImpl(get()) }

}