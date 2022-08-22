package com.example.myapplication.ui.presentation.cities_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.BuildConfig
import com.example.myapplication.data.network.api.OpenWeatherApi
import com.example.myapplication.data.network.models.OpenWeatherResponse
import com.example.myapplication.data.repositories.CitiesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CitiesDetailVM @Inject constructor(
    private val repository: CitiesRepository,
    private val openWeatherApi: OpenWeatherApi
) : ViewModel() {
    private val weatherResponse: MutableSharedFlow<OpenWeatherResponse?> = MutableSharedFlow()
    fun getWeatherResponse(): Flow<OpenWeatherResponse?> = weatherResponse

    fun requestOpenWeatherApi(id: Int) {
        viewModelScope.launch {
            val currentCitie = repository.getCitie(id)
            val currentWeather = openWeatherApi.getCurrentWeatherData(
                currentCitie.coord.lon.toString(),
                currentCitie.coord.lat.toString(),
                BuildConfig.API_KEY
            )
            currentWeather.enqueue(object : Callback<OpenWeatherResponse?> {
                override fun onResponse(
                    call: Call<OpenWeatherResponse?>,
                    response: Response<OpenWeatherResponse?>
                ) {
                    if (response.isSuccessful) {
                        viewModelScope.launch {
                            weatherResponse.emit(response.body())
                        }
                        Timber.d("Current Weather : ${ response.body()}")
                    } else {
                        Timber.d("Error on fetching Weather")
                    }
                }

                override fun onFailure(call: Call<OpenWeatherResponse?>, t: Throwable) {
                    Timber.d("Error on fetching Weather")
                }
            })
        }
    }
}