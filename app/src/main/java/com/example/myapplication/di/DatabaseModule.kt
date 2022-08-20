package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.room.CitiesRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideCitiesRoomDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, CitiesRoomDatabase::class.java, "cities_database")
            .createFromAsset("test.db").build()

    @Provides
    @Singleton
    fun provideCitiesDao(database: CitiesRoomDatabase) = database.citiesDao()
}