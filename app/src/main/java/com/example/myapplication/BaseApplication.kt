package com.example.myapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {
    companion object {
        var BASE_URL = "https://api.openweathermap.org/data/2.5/"
    }
}