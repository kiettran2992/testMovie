package com.example.mymovies.data.model.api

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @PrimaryKey()
    val id: Int,

    @ColumnInfo(name = "title")
    val title: String = "",

    @SerializedName("poster_path")
    @ColumnInfo(name = "poster_path")
    val posterPath: String? = null,

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    val releaseDate: String = "",

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    val voteAverage: String = "",

    @ColumnInfo(name = "page")
    var page: Int?
)