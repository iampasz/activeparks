package com.app.activeparks.di

import com.app.activeparks.data.repository.clubs.ClubsRepository
import com.app.activeparks.data.repository.clubs.ClubsRepositoryImpl
import com.app.activeparks.data.repository.gallery.GalleryStateRepository
import com.app.activeparks.data.repository.gallery.GalleryStateRepositoryImpl
import com.app.activeparks.data.repository.ctiveState.ActivityStateRepository
import com.app.activeparks.data.repository.ctiveState.ActivityStateRepositoryImpl
import com.app.activeparks.data.repository.eventState.EventStateRepository
import com.app.activeparks.data.repository.eventState.EventStateRepositoryImpl
import com.app.activeparks.data.repository.news.NewsRepository
import com.app.activeparks.data.repository.news.NewsRepositoryImpl
import com.app.activeparks.data.repository.pauseActivity.PauseActivityRepository
import com.app.activeparks.data.repository.pauseActivity.PauseActivityRepositoryImpl
import com.app.activeparks.data.repository.profileState.ProfileStateRepository
import com.app.activeparks.data.repository.profileState.ProfileStateRepositoryImpl
import com.app.activeparks.data.repository.registratio.UserRepository
import com.app.activeparks.data.repository.registratio.UserRepositoryImpl
import com.app.activeparks.data.repository.routeActive.RouteActiveStateRepository
import com.app.activeparks.data.repository.routeActive.RouteActiveStateRepositoryImpl
import com.app.activeparks.data.repository.saveActivity.SaveActivityRepository
import com.app.activeparks.data.repository.saveActivity.SaveActivityRepositoryImpl
import com.app.activeparks.data.repository.sesion.MobileApiSessionRepository
import com.app.activeparks.data.repository.sesion.SharedPreferencesMobileApiSessionRepository
import com.app.activeparks.data.repository.statistics.StatisticsRepository
import com.app.activeparks.data.repository.statistics.StatisticsRepositoryImpl
import com.app.activeparks.data.repository.track.TrackStateRepository
import com.app.activeparks.data.repository.track.TrackStateRepositoryImpl
import com.app.activeparks.data.repository.uploadFile.UploadFileRepository
import com.app.activeparks.data.repository.uploadFile.UploadFileRepositoryImpl
import com.app.activeparks.data.repository.video.VideoRepository
import com.app.activeparks.data.repository.video.VideoRepositoryImpl
import com.app.activeparks.data.repository.weather.WeatherRepository
import com.app.activeparks.data.repository.weather.WeatherRepositoryImpl
import org.koin.dsl.module

/**
 * Created by O.Dziuba on 07.11.2023.
 */

val repositoryModule = module{
        single<SaveActivityRepository> { SaveActivityRepositoryImpl(get(), get()) }
        single<PauseActivityRepository> { PauseActivityRepositoryImpl(get()) }
        single<ActivityStateRepository> { ActivityStateRepositoryImpl(get()) }

        single<EventStateRepository> { EventStateRepositoryImpl(get(), get()) }
        single<ProfileStateRepository> { ProfileStateRepositoryImpl(get()) }
        single<WeatherRepository> { WeatherRepositoryImpl(get()) }
        single<StatisticsRepository> { StatisticsRepositoryImpl(get()) }
        single<UploadFileRepository> { UploadFileRepositoryImpl(get()) }
        single<VideoRepository> { VideoRepositoryImpl(get()) }
        single<MobileApiSessionRepository> { SharedPreferencesMobileApiSessionRepository(get()) }
        single<UserRepository> { UserRepositoryImpl(get(), get()) }
        single<GalleryStateRepository> { GalleryStateRepositoryImpl(get()) }
        single<NewsRepository> { NewsRepositoryImpl(get()) }

        single<TrackStateRepository> { TrackStateRepositoryImpl(get()) }
        single<RouteActiveStateRepository> { RouteActiveStateRepositoryImpl(get()) }
        single<ClubsRepository> { ClubsRepositoryImpl(get()) }
}