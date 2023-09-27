package com.example.mymovies.data.repository.datasource.local

import androidx.paging.PagingSource
import com.example.mymovies.data.model.db.Movie
import com.example.mymovies.data.model.db.MovieDetail
import com.example.mymovies.data.model.db.RemoteKeys

interface MovieLocalSource {
    fun getTrendingMoviePagingSource(): PagingSource<Int, Movie>
    suspend fun getMovieDetail(movieId: Int): MovieDetail?
    suspend fun insertMovieDetail(movieDetail: MovieDetail)
    suspend fun insertMovies(movies: List<Movie>)
    suspend fun deleteAllMovies()

    suspend fun getRemoteKeyByMovieID(movieId: Int): RemoteKeys?
    suspend fun getCreatedAt(): Long?
    suspend fun deleteAllRemoteKeys()
    suspend fun insertRemoteKeys(remoteKeys: List<RemoteKeys>)
}