package com.example.mymovies.data.model.db.converters

import androidx.room.TypeConverter
import com.example.mymovies.data.model.db.Genres
import com.example.mymovies.data.model.db.ProductionCompany
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MovieDetailConverter {
    @TypeConverter
    fun toGenres(list: String): List<Genres> {
        return Gson().fromJson(list, object : TypeToken<List<Genres>>() {}.type)
    }

    @TypeConverter
    fun fromGenres(list: List<Genres>): String{
        return Gson().toJson(list, object : TypeToken<List<Genres>>() {}.type)
    }

    @TypeConverter
    fun toProductionCompanies(list: String): List<ProductionCompany> {
        return Gson().fromJson(list, object : TypeToken<List<ProductionCompany>>() {}.type)
    }

    @TypeConverter
    fun fromProductionCompanies(list: List<ProductionCompany>): String{
        return Gson().toJson(list, object : TypeToken<List<ProductionCompany>>() {}.type)
    }
}