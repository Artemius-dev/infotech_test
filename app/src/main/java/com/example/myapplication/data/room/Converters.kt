package com.example.myapplication.data.room

import androidx.room.TypeConverter
import com.example.myapplication.data.room.models.CoordModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object Converters {
    @TypeConverter
    fun fromJsonToArrayList(value: String?): CoordModel {
        val listType: Type = object : TypeToken<CoordModel?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayListToJson(list: CoordModel): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}