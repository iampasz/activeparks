package com.app.activeparks.di

import com.app.activeparks.data.network.NetworkManager
import com.app.activeparks.data.network.NetworkManagerImpl
import com.app.activeparks.data.network.baseNew.ApiWithAuthorization
import com.app.activeparks.data.network.baseNew.ApiWithOutAuthorization
import com.app.activeparks.data.network.weather.ApiWeather
import com.app.activeparks.data.repository.sesion.MobileApiSessionRepository
import com.google.gson.GsonBuilder
import com.technodreams.activeparks.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by O.Dziuba on 27.11.2023.
 */

private const val APP_ID = "appid"
private const val API_KEY_WEATHER = "a2bc3dabded854b32f756dd70997da5e"
private const val BASE_URL_WEATHER = "https://api.openweathermap.org/data/2.5/"
//private const val BASE_URL = "https://ap.sportforall.gov.ua"
private const val TEST_URL = "https://ap-dev.sportforall.gov.ua"
private const val HTTP_LOGGING_INTERCEPTOR = "http_logging_interceptor"
private const val WEATHER = "weather"

private const val TIMEOUT_SEC: Long = 60


private const val WITH_AUTH: String = "WITH_AUTH"
private const val WITHOUT_AUTH: String = "WITHOUT_AUTH"


private const val TOKEN = "token"
private const val AUTHORIZATION = "Authorization"

val networkModule = module {


    single(named(APP_ID)) {
        Interceptor { chain ->
            val originalRequest = chain.request()
            val newUrl = originalRequest.url.newBuilder()
                .addQueryParameter(APP_ID, API_KEY_WEATHER)
                .build()

            val newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }
    }

    single<Interceptor>(named(HTTP_LOGGING_INTERCEPTOR)) {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }

    single(named(WEATHER)) {
        OkHttpClient
            .Builder()
            .addInterceptor(get<Interceptor>(named(HTTP_LOGGING_INTERCEPTOR)))
            .addInterceptor(get<Interceptor>(named(APP_ID)))
            .connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .build()
    }

    single(named(WEATHER)) {
        Retrofit.Builder()
            .baseUrl(BASE_URL_WEATHER)
            .client(get(named(WEATHER)))
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .build()
    }


    single(named(WEATHER)) {
        (get((named(WEATHER))) as Retrofit).create(ApiWeather::class.java)
    }

    single<NetworkManager> {
        NetworkManagerImpl(
            get(named(WEATHER)),
            get(named(WITH_AUTH)),
            get(named(WITHOUT_AUTH)),
            androidContext()
        )
    }
    single<Interceptor>(named(HTTP_LOGGING_INTERCEPTOR)) {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }

    single(named(TOKEN)) {
        Interceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader(
                    AUTHORIZATION,
                    "Bearer ${(get() as MobileApiSessionRepository).load().accessToken()}"
                )
                .build()
            chain.proceed(newRequest)
        }
    }

    single(named(WITH_AUTH)) {
        OkHttpClient
            .Builder()
            .addInterceptor(get<Interceptor>(named(HTTP_LOGGING_INTERCEPTOR)))
            .addInterceptor(get<Interceptor>(named(TOKEN)))
            .connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .build()
    }

    single(named(WITHOUT_AUTH)) {
        OkHttpClient
            .Builder()
            .addInterceptor(get<Interceptor>(named(HTTP_LOGGING_INTERCEPTOR)))
            .connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
            .build()
    }

    single(named(WITH_AUTH)) {
        Retrofit.Builder()
            .baseUrl(TEST_URL)
            .client(get(named(WITH_AUTH)))
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .build()
    }

    single(named(WITHOUT_AUTH)) {
        Retrofit.Builder()
            .baseUrl(TEST_URL)
            .client(get(named(WITHOUT_AUTH)))
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .build()
    }


    single(named(WITH_AUTH)) {
        (get((named(WITH_AUTH))) as Retrofit).create(ApiWithAuthorization::class.java)
    }

    single(named(WITHOUT_AUTH)) {
        (get((named(WITHOUT_AUTH))) as Retrofit).create(ApiWithOutAuthorization::class.java)
    }
}