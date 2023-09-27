package com.example.mymovies.data.repository.page

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.mymovies.data.local.db.AppDatabase
import com.example.mymovies.data.model.ErrorState
import com.example.mymovies.data.model.api.mapper.toMovies
import com.example.mymovies.data.model.db.Movie
import com.example.mymovies.data.model.db.RemoteKeys
import com.example.mymovies.data.repository.datasource.local.MovieLocalSource
import com.example.mymovies.data.repository.datasource.remote.MovieRemoteSource
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class TrendingMovieSource(
    private val movieRemoteSource: MovieRemoteSource,
    private val movieLocalSource: MovieLocalSource,
    private val appDatabase: AppDatabase
) :  RemoteMediator<Int, Movie>()  {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

        return if (System.currentTimeMillis() - (movieLocalSource.getCreatedAt() ?: 0) < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { movie ->
            movieLocalSource.getRemoteKeyByMovieID(movie.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { movie ->
            movieLocalSource.getRemoteKeyByMovieID(movie.id)
        }
    }


    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Movie>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                movieLocalSource.getRemoteKeyByMovieID(id)
            }
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Movie>
    ): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {
            val apiResponse = movieRemoteSource.getTodayTrendingMovie(pageNumber = page)

            val movies = apiResponse.results.toMovies()
            val endOfPaginationReached = movies.isEmpty()

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieLocalSource.deleteAllRemoteKeys()
                    movieLocalSource.deleteAllMovies()
                }
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (endOfPaginationReached) null else page + 1
                val remoteKeys = movies.map {
                    RemoteKeys(movieID = it.id, prevKey = prevKey, currentPage = page, nextKey = nextKey)
                }

                movieLocalSource.insertRemoteKeys(remoteKeys)
                movieLocalSource.insertMovies(movies.onEachIndexed { _, movie -> movie.page = page })
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (error: ErrorState) {
            return MediatorResult.Error(error)
        }
    }
}