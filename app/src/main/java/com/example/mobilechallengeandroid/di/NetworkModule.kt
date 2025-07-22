package com.example.mobilechallengeandroid.di

import com.example.mobilechallengeandroid.data.remote.WeatherApi
import com.example.mobilechallengeandroid.data.remote.FileDownloadApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://weather.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi =
        retrofit.create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideFileDownloadApi(okHttpClient: OkHttpClient): FileDownloadApi =
        Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/") // baseUrl dummy
            .client(okHttpClient)
            .build()
            .create(FileDownloadApi::class.java)
}