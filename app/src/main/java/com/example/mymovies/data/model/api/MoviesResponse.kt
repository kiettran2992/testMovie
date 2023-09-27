package com.example.mymovies.data.model.api

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("total_results")
    val totalResults: Int,

    @SerializedName("results")
    val results: List<MovieResponse>,

    @SerializedName("page")
    val page: Int
)