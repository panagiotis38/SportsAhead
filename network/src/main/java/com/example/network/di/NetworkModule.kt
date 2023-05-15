package com.example.network.di

import com.example.network.BuildConfig
import com.example.network.api.ApiConstants
import com.example.network.api.ApiService
import com.example.network.interceptors.CurlLogger
import com.example.network.interceptors.CurlLoggerInterceptor
import com.example.network.repo.UpcomingEventsNetworkRepo
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideUpcomingEventsRepo(): UpcomingEventsNetworkRepo {
        return UpcomingEventsNetworkRepo(getApiInstance())
    }

    companion object {
        lateinit var apiService: ApiService

        fun getApiInstance(): ApiService {
            if (this::apiService.isInitialized.not()) {
                apiService = provideApiService()
            }
            return apiService
        }

        private fun provideApiService(): ApiService {
            return provideRetrofit().create(ApiService::class.java)
        }

        private fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(ApiConstants.RETROFIT_BASE_URL)
                .client(provideOkHttpClient())
                .addConverterFactory(provideGsonConverterFactory())
                .build()
        }

        private fun provideOkHttpClient() = OkHttpClient.Builder().also {
            it.addNetworkInterceptor(provideCurlLogger())
            it.addNetworkInterceptor(provideHttpLogger())
            it.connectTimeout(60L, TimeUnit.SECONDS)
            it.readTimeout(60L, TimeUnit.SECONDS)
            it.writeTimeout(60L, TimeUnit.SECONDS)
        }.build()

        private fun provideCurlLogger() = CurlLoggerInterceptor(
            loggable = CurlLogger(
                level = if (BuildConfig.DEBUG) {
                    Level.INFO
                } else {
                    Level.OFF
                }
            )
        )

        private fun provideHttpLogger() = HttpLoggingInterceptor().also {
            it.level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        private fun provideGsonConverterFactory() = GsonConverterFactory.create()

    }
}