package com.example.myapplication.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {
    companion object {
        var BASE_URL = "https://s3-us-west-2.amazonaws.com/"
    }
}