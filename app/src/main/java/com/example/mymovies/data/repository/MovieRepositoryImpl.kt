package com.example.mymovies.data.repository

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mymovies.data.local.db.AppDatabase
import com.example.mymovies.data.model.ErrorState
import com.example.mymovies.data.model.api.mapper.toMovieDetail
import com.example.mymovies.data.model.db.Movie
import com.example.mymovies.data.model.db.MovieDetail
import com.example.mymovies.data.repository.datasource.local.MovieLocalSource
import com.example.mymovies.data.repository.datasource.remote.MovieRemoteSource
import com.example.mymovies.data.repository.page.SearchMovieSource
import com.example.mymovies.data.repository.page.TrendingMovieSource
import com.example.mymovies.domain.repository.MovieRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val movieRemoteSource: MovieRemoteSource,
    private val movieLocalSource: MovieLocalSource,
    private val appDatabase: AppDatabase,
    @ApplicationContext private val context: Context
) : MovieRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getTrendingMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                initialLoadSize = 5
            ),
            pagingSourceFactory = {
                movieLocalSource.getTrendingMoviePagingSource()
            },
            remoteMediator = TrendingMovieSource(
                movieRemoteSource,
                movieLocalSource,
                appDatabase
            )
        ).flow
    }

    override suspend fun searchMovies(searchText: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, prefetchDistance = 2),
            pagingSourceFactory = {
                SearchMovieSource(movieRemoteSource, searchText)
            }
        ).flow
    }

    override suspend fun getMovieDetail(movieId: Int): RepoResponse<MovieDetail> {
        return movieLocalSource.getMovieDetail(movieId = movieId)?.let {
            return RepoResponse.Success(it)
        } ?: run {
            try {
                val movieDetail = movieRemoteSource.getMovieDetail(movieId = movieId).toMovieDetail()
                movieLocalSource.insertMovieDetail(movieDetail)
                RepoResponse.Success(
                    movieDetail
                )
            } catch (errorState: ErrorState) {
                RepoResponse.Error(errorState)
            }
        }
    }

}