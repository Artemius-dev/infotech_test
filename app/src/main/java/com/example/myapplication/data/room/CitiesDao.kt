package com.example.myapplication.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.room.models.CitiesModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CitiesDao {
    @Query("SELECT * FROM cities_table WHERE id == 833")
    fun getSortedCities(): Flow<List<CitiesModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(citie: CitiesModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(citiesList: Iterable<CitiesModel>)

    @Query("DELETE FROM cities_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM cities_table")
    suspend fun getAllCities(): List<CitiesModel>

    @Query("SELECT * FROM cities_table WHERE citie_id = :citieId")
    suspend fun getCitie(citieId: Int): CitiesModel

    @Query("SELECT * FROM cities_table WHERE name LIKE :citieName LIMIT :limitRangeStart, :limitPerPage")
    suspend fun getCitieWithName(citieName: String, limitRangeStart: Int, limitPerPage: Int): List<CitiesModel>

    @Query("SELECT * FROM cities_table LIMIT :limitRangeStart, :limitPerPage")
    suspend fun getCitiesInRange(limitRangeStart: Int, limitPerPage: Int): List<CitiesModel>

    @Query("SELECT * FROM cities_table WHERE country LIKE :countryName LIMIT :limitRangeStart, :limitPerPage")
    suspend fun getCitiesByCountry(countryName: String, limitRangeStart: Int, limitPerPage: Int): List<CitiesModel>

    @Query("SELECT * FROM cities_table GROUP BY country LIMIT :limitRangeStart, :limitPerPage")
    suspend fun getCountryInRange(limitRangeStart: Int, limitPerPage: Int): List<CitiesModel>

    @Query("SELECT * FROM cities_table WHERE country LIKE :countryName GROUP BY country LIMIT :limitRangeStart, :limitPerPage")
    suspend fun getCountryWithName(countryName: String, limitRangeStart: Int, limitPerPage: Int): List<CitiesModel>

    @Query("SELECT * FROM cities_table WHERE id = :id")
    suspend fun isDataExist(id: Int): CitiesModel
}