package com.app.activeparks.di

import com.app.activeparks.data.useCase.saveActivity.SaveActivityUseCase
import com.app.activeparks.data.useCase.saveActivity.SaveActivityUseCaseImpl
import org.koin.dsl.module

/**
 * Created by O.Dziuba on 09.11.2023.
 */

val useCaseModule = module {
    single<SaveActivityUseCase> { SaveActivityUseCaseImpl(get()) }
}