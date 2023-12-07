package com.app.activeparks.di

import com.app.activeparks.data.useCase.activeState.ActivityStateUseCase
import com.app.activeparks.data.useCase.activeState.ActivityStateUseCaseImpl
import com.app.activeparks.data.useCase.registration.UserUseCase
import com.app.activeparks.data.useCase.registration.UserUseCaseImpl
import com.app.activeparks.data.useCase.saveActivity.SaveActivityUseCase
import com.app.activeparks.data.useCase.saveActivity.SaveActivityUseCaseImpl
import com.app.activeparks.data.useCase.weatehr.WeatherUseCase
import com.app.activeparks.data.useCase.weatehr.WeatherUseCaseImpl
import org.koin.dsl.module

/**
 * Created by O.Dziuba on 09.11.2023.
 */

val useCaseModule = module {
    single<SaveActivityUseCase> { SaveActivityUseCaseImpl(get()) }
    single<ActivityStateUseCase> { ActivityStateUseCaseImpl(get()) }
    single<WeatherUseCase> { WeatherUseCaseImpl(get()) }
    single<UserUseCase> { UserUseCaseImpl(get(), get(), get()) }
}