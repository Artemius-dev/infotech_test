package com.example.myapplication.data.network.api

import com.example.myapplication.data.network.models.OpenWeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET("weather?")
    fun getCurrentWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") app_id: String
    ): Call<OpenWeatherResponse>
}
