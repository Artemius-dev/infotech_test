package com.example.myapplication.ui

import androidx.annotation.WorkerThread
import com.example.myapplication.data.paging.CitieRequestState
import com.example.myapplication.data.paging.CitiesPagingSource
import com.example.myapplication.data.room.CitiesDao
import com.example.myapplication.data.room.models.CitiesModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CitiesRepository @Inject constructor(private val citiesDao: CitiesDao) {

    fun citiesPagingSource(citieRequestState: CitieRequestState = CitieRequestState.DEFAULT, citieNameCountry: String = "") = CitiesPagingSource(this, citieRequestState, citieNameCountry)

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getCitie(id: Int): CitiesModel {
        return citiesDao.getCitie(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getCitieWithName(citieName: String, limitRangeStart: Int, limitPerPage: Int = ITEMS_PER_PAGE): List<CitiesModel> {
        return citiesDao.getCitieWithName(citieName, limitRangeStart, limitPerPage)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun filterCitiesByCountry(countryName: String, limitRangeStart: Int, limitPerPage: Int = ITEMS_PER_PAGE): List<CitiesModel> {
        return citiesDao.filterCitiesByCountry(countryName, limitRangeStart, limitPerPage)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getCitiesInRange(startId: Int, limitPerPage: Int = ITEMS_PER_PAGE): List<CitiesModel> {
        return citiesDao.getCitiesInRange(startId, limitPerPage)
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