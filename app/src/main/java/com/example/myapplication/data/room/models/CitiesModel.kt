package com.example.myapplication.data.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.myapplication.data.room.Converters

@TypeConverters(Converters::class)
@Entity(tableName = "cities_table")
data class CitiesModel(
    @PrimaryKey @ColumnInfo(name = "id") val id : Int,
    val name: String,
    val state: String,
    val country: String,
    val coord: CoordModel
)
