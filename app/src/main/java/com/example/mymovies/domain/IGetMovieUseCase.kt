package com.example.mymovies.domain

import androidx.paging.PagingData
import com.example.mymovies.data.model.db.Movie
import kotlinx.coroutines.flow.Flow

interface IGetMovieUseCase {
    suspend fun execute(query: String): Flow<PagingData<Movie>>
}