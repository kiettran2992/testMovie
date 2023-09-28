package com.example.mymovies.data.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_detail")
data class MovieDetail(
    @PrimaryKey()
    val id: Int,
    val originalLanguage: String?,
    val posterPath: String?,
    val backdropPath: String?,
    val originalTitle: String?,
    val overview: String?,
    val releaseDate: String?,
    val revenue: Long?,
    val runTime: Int?,
    val status: String?,
    val voteAverage: Double?,
    val voteCount: Int,
    val homePage: String?,
    val genres: List<Genres>?,
    val productionCompanies: List<ProductionCompany>?
)

data class Genres(
    val id: Int,
    val name: String
)

data class ProductionCompany(
    val id: Int,
    val logoPath: String?,
    val name: String,
    val originCountry: String
)