package com.example.mobilechallengeandroid.di

import com.example.mobilechallengeandroid.data.network.weather.WeatherApiClient
import com.example.mobilechallengeandroid.data.network.file.CityListApiClient
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
    fun provideWeatherApi(okHttpClient: OkHttpClient): WeatherApiClient =
        Retrofit.Builder()
            .baseUrl("https://weather.googleapis.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiClient::class.java)

    @Provides
    @Singleton
    fun provideFileDownloadApi(okHttpClient: OkHttpClient): CityListApiClient =
        Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/hernan-uala/dce8843a8edbe0b0018b32e137bc2b3a/raw/0996accf70cb0ca0e16f9a99e0ee185fafca7af1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CityListApiClient::class.java)
}