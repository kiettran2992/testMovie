package com.example.mymovies.domain

import androidx.paging.PagingData
import com.example.mymovies.data.model.db.Movie
import com.example.mymovies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieUseCase @Inject constructor(private val movieRepository: MovieRepository):
    IGetMovieUseCase {
    override suspend fun execute(query: String): Flow<PagingData<Movie>> {
        return if (query.isEmpty()) {
            movieRepository.getTrendingMovies()
        } else {
            movieRepository.searchMovies(query)
        }
    }
}