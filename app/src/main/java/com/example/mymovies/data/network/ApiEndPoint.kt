package com.example.mymovies.data.network

import com.example.mymovies.data.model.api.MovieDetailResponse
import com.example.mymovies.data.model.api.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiEndPoint {

    @GET("3/trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("page") pageNumber: Int,
        @Query("language") language: String
    ): MoviesResponse

    @GET("3/search/movie")
    suspend fun searchMovies(
        @Query("page") pageNumber: Int,
        @Query("query") query: String,
        @Query("language") language: String
    ): MoviesResponse

    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String
    ): MovieDetailResponse
}