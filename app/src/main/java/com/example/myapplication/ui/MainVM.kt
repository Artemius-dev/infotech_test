package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.myapplication.data.room.models.CitiesModel
import com.example.myapplication.data.room.CitiesRoomDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val ITEMS_PER_PAGE = 20

@HiltViewModel
class MainVM @Inject constructor(private val repository: CitiesRepository) : ViewModel() {

    val items: Flow<PagingData<CitiesModel>> = Pager(
        config = PagingConfig(pageSize = ITEMS_PER_PAGE, enablePlaceholders = false),
        pagingSourceFactory = { repository.citiesPagingSource() }
    ).flow.cachedIn(viewModelScope)

    fun readJson(jsonFileString: String?) {
        viewModelScope.launch {
            val gson = Gson()
            val listCitiesType = object : TypeToken<List<CitiesModel>>() {}.type
            val citiesList: List<CitiesModel> = gson.fromJson(jsonFileString, listCitiesType)

            // TODO: Check whether the data is already present
            citiesList.let { repository.insertAll(it) }
        }
    }
}