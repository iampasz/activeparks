package com.app.activeparks.di

import com.app.activeparks.data.useCase.activeState.ActivityStateUseCase
import com.app.activeparks.data.useCase.activeState.ActivityStateUseCaseImpl
import com.app.activeparks.data.useCase.photoGallery.PhotoGalleryUseCase
import com.app.activeparks.data.useCase.photoGallery.PhotoGalleryUseCaseImpl
import com.app.activeparks.data.useCase.registration.UserUseCase
import com.app.activeparks.data.useCase.registration.UserUseCaseImpl
import com.app.activeparks.data.useCase.saveActivity.SaveActivityUseCase
import com.app.activeparks.data.useCase.saveActivity.SaveActivityUseCaseImpl
import com.app.activeparks.data.useCase.eventState.EventStateUseCase
import com.app.activeparks.data.useCase.eventState.EventStateUseCaseImpl
import com.app.activeparks.data.useCase.pauseActivity.PauseActivityUseCase
import com.app.activeparks.data.useCase.pauseActivity.PauseActivityUseCaseImpl
import com.app.activeparks.data.useCase.profileState.ProfileStateUseCase
import com.app.activeparks.data.useCase.profileState.ProfileStateUseCaseImpl
import com.app.activeparks.data.useCase.statistics.StatisticsUseCase
import com.app.activeparks.data.useCase.statistics.StatisticsUseCaseImpl
import com.app.activeparks.data.useCase.uploadFile.UploadFileUseCase
import com.app.activeparks.data.useCase.uploadFile.UploadFileUseCaseImpl
import com.app.activeparks.data.useCase.video.VideoUseCase
import com.app.activeparks.data.useCase.video.VideoUseCaseImpl
import com.app.activeparks.data.useCase.weatehr.WeatherUseCase
import com.app.activeparks.data.useCase.weatehr.WeatherUseCaseImpl
import org.koin.dsl.module

/**
 * Created by O.Dziuba on 09.11.2023.
 */

val useCaseModule = module {
    single<SaveActivityUseCase> { SaveActivityUseCaseImpl(get()) }
    single<PauseActivityUseCase> { PauseActivityUseCaseImpl(get()) }
    single<ActivityStateUseCase> { ActivityStateUseCaseImpl(get()) }
    single<EventStateUseCase> { EventStateUseCaseImpl(get()) }
    single<ProfileStateUseCase> { ProfileStateUseCaseImpl(get()) }

    single<WeatherUseCase> { WeatherUseCaseImpl(get()) }
    single<VideoUseCase> { VideoUseCaseImpl(get()) }
    single<StatisticsUseCase> { StatisticsUseCaseImpl(get()) }
    single<UploadFileUseCase> { UploadFileUseCaseImpl(get()) }
    single<UserUseCase> { UserUseCaseImpl(get(), get(), get()) }
    single<PhotoGalleryUseCase> { PhotoGalleryUseCaseImpl(get()) }
}