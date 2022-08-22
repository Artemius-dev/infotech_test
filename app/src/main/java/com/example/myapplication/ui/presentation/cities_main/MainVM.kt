package com.example.myapplication.ui.presentation.cities_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.myapplication.R
import com.example.myapplication.data.paging.CitieRequestState
import com.example.myapplication.data.room.models.CitiesModel
import com.example.myapplication.data.repositories.CitiesRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

const val ITEMS_PER_PAGE = 5

@HiltViewModel
class MainVM @Inject constructor(private val repository: CitiesRepository) : ViewModel() {
    init {
        showAllCities()
    }

    lateinit var items: Flow<PagingData<CitiesModel>>
    lateinit var filterItems: Flow<PagingData<CitiesModel>>

    private val retryState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    fun getRetryState(): Flow<Boolean> = retryState

    fun readJson(jsonFileString: String?) {
        viewModelScope.launch {
            val gson = Gson()
            val listCitiesType = object : TypeToken<List<CitiesModel>>() {}.type
            val citiesList: List<CitiesModel> = gson.fromJson(jsonFileString, listCitiesType)
            if (!repository.getAllCities().containsAll(citiesList)) {
                citiesList.let { repository.insertAll(it) }
                showAllCities()
                retryState.emit(true)
            }
        }
    }

    fun setCitieImage(citieId: Int): Int {
        return if (citieId % 2 == 0) {
            R.string.even_image_st
        } else {
            R.string.odd_image_st
        }
    }

    fun showAllCities() {
        items = Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE, enablePlaceholders = false),
            pagingSourceFactory = { repository.citiesPagingSource() }
        ).flow.cachedIn(viewModelScope)
    }

    fun searchCities(searchName: String) {
        items = Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE, enablePlaceholders = false),
            pagingSourceFactory = {
                repository.citiesPagingSource(
                    CitieRequestState.SEARCH,
                    searchName
                )
            }
        ).flow.cachedIn(viewModelScope)
    }

    fun getAllCountryNames() {
        filterItems = Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE, enablePlaceholders = false),
            pagingSourceFactory = { repository.citiesPagingSource(CitieRequestState.FILTER) }
        ).flow.cachedIn(viewModelScope)
    }

    fun getContryByName(countryName: String) {
        filterItems = Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE, enablePlaceholders = false),
            pagingSourceFactory = {
                repository.citiesPagingSource(
                    CitieRequestState.COUNTRY_FILTER,
                    countryName
                )
            }
        ).flow.cachedIn(viewModelScope)
    }

    fun getCitiesByCountry(filterCountry: String) {

        items = Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE, enablePlaceholders = false),
            pagingSourceFactory = {
                repository.citiesPagingSource(
                    CitieRequestState.CITIES_BY_COUNTRY,
                    filterCountry
                )
            }
        ).flow.cachedIn(viewModelScope)
    }
}