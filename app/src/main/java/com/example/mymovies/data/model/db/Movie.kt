package com.example.mymovies.data.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey()
    val id: Int,

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "poster_path")
    val posterPath: String? = null,

    @ColumnInfo(name = "release_date")
    val releaseDate: String = "",

    @ColumnInfo(name = "vote_average")
    val voteAverage: String = "",

    @ColumnInfo(name = "page")
    var page: Int?
)
