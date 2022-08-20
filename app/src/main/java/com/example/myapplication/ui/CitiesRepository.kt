package com.example.myapplication.ui

import androidx.annotation.WorkerThread
import com.example.myapplication.data.paging.CitiesPagingSource
import com.example.myapplication.data.room.CitiesDao
import com.example.myapplication.data.room.models.CitiesModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CitiesRepository @Inject constructor(private val citiesDao: CitiesDao) {

    val allCities: Flow<List<CitiesModel>> = citiesDao.getSortedCities()

    fun citiesPagingSource() = CitiesPagingSource(this)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getCitie(id: Int): CitiesModel {
        return citiesDao.getCitie(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(cities: CitiesModel) {
        citiesDao.insert(cities)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertAll(citiesList: Iterable<CitiesModel>) {
        citiesDao.insertAll(citiesList)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun isDataExist(id: Int): CitiesModel {
        return citiesDao.isDataExist(id)
    }
}