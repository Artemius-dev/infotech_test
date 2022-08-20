package com.example.myapplication.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.data.room.models.CitiesModel

@Database(entities = [CitiesModel::class], version = 1, exportSchema = false)
abstract class CitiesRoomDatabase : RoomDatabase() {

    abstract fun citiesDao(): CitiesDao
}