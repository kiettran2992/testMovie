package com.example.mymovies.data.repository.datasource.local

import androidx.paging.PagingSource
import com.example.mymovies.data.local.db.dao.MovieDao
import com.example.mymovies.data.local.db.dao.MovieDetailDao
import com.example.mymovies.data.local.db.dao.RemoteKeysDao
import com.example.mymovies.data.model.db.Movie
import com.example.mymovies.data.model.db.MovieDetail
import com.example.mymovies.data.model.db.RemoteKeys
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieLocalSourceImpl @Inject constructor(private val movieDao: MovieDao,private val movieDetailDao: MovieDetailDao, private val remoteKeysDao: RemoteKeysDao): MovieLocalSource {
    override fun getTrendingMoviePagingSource(): PagingSource<Int, Movie> {
        return movieDao.getMoviesPaging()
    }

    override suspend fun getMovieDetail(movieId: Int): MovieDetail? {
        return movieDetailDao.getMovieDetail(movieId)
    }

    override suspend fun insertMovieDetail(movieDetail: MovieDetail) {
        movieDetailDao.insertMovieDetail(movieDetail = movieDetail)
    }

    override suspend fun insertMovies(movies: List<Movie>) {
        movieDao.insertMovies(movies)
    }

    override suspend fun deleteAllMovies() {
        movieDao.deleteAll()
    }

    override suspend fun getRemoteKeyByMovieID(movieId: Int): RemoteKeys? {
        return remoteKeysDao.getRemoteKeyByMovieID(movieId)
    }

    override suspend fun getCreatedAt(): Long? {
        return remoteKeysDao.getCreatedAt()
    }

    override suspend fun deleteAllRemoteKeys() {
        remoteKeysDao.deleteAll()
    }

    override suspend fun insertRemoteKeys(remoteKeys: List<RemoteKeys>) {
        remoteKeysDao.insertAll(remoteKeys)
    }
}