package com.example.mymovies.data.repository.datasource.remote

import com.example.mymovies.data.model.api.MovieDetailResponse
import com.example.mymovies.data.model.api.MoviesResponse

interface MovieRemoteSource {
    suspend fun getTodayTrendingMovie(pageNumber: Int): MoviesResponse
    suspend fun getMovieDetail(movieId: Int): MovieDetailResponse
    suspend fun searchMovie(query: String, pageNumber: Int): MoviesResponse
}