package com.example.myapplication.ui.callbacks

import android.widget.ImageView
import com.example.myapplication.data.room.models.CitiesModel

interface NavigationCallback {
    fun navigateToCitiesDetails(citiesModel: CitiesModel)
    fun setCitieImage(citieId: Int, imageView: ImageView)
}