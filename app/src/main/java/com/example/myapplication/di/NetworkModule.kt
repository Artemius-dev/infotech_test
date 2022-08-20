package com.example.myapplication.di

import com.example.myapplication.BaseApplication.Companion.BASE_URL
import com.example.myapplication.data.network.api.OpenWeatherApi
import com.ihsanbal.logging.LoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import timber.log.Timber
import com.ihsanbal.logging.Logger as LoggingLogger

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): LoggingInterceptor {
        return LoggingInterceptor.Builder().logger(object : LoggingLogger {
            override fun log(level: Int, tag: String?, msg: String?) {
                Timber.tag(tag ?: "").d(msg)
            }
        }).setLevel(com.ihsanbal.logging.Level.BASIC)
            .build()
    }

    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: LoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideOpenWeatherApi(retrofitClient: Retrofit): OpenWeatherApi {
        return retrofitClient
            .create(OpenWeatherApi::class.java)
    }
}