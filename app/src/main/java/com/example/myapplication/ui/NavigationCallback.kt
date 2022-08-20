package com.example.myapplication.ui

import com.example.myapplication.data.room.models.CitiesModel

interface NavigationCallback {
    fun navigateToCitiesDetails(citiesModel: CitiesModel)
}