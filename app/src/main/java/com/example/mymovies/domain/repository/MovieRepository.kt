package com.example.mymovies.domain.repository

import androidx.paging.PagingData
import com.example.mymovies.data.model.db.Movie
import com.example.mymovies.data.model.db.MovieDetail
import com.example.mymovies.data.repository.RepoResponse
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getTrendingMovies(): Flow<PagingData<Movie>>
    suspend fun searchMovies(searchText: String): Flow<PagingData<Movie>>
    suspend fun getMovieDetail(movieId: Int): RepoResponse<MovieDetail>
}