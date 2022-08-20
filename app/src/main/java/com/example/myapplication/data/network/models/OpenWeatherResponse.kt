package com.example.myapplication.data.network.models

data class OpenWeatherResponse(
    val weather: List<WeatherModel>?,
    val main: MainModel?,
    val wind: WindModel?
)