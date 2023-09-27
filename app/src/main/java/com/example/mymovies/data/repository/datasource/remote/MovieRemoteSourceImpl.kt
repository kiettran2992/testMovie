package com.example.mymovies.data.repository.datasource.remote

import android.content.Context
import android.os.LocaleList
import com.example.mymovies.data.model.ErrorState
import com.example.mymovies.data.model.api.MovieDetailResponse
import com.example.mymovies.data.model.api.MoviesResponse
import com.example.mymovies.data.network.ApiEndPoint
import com.example.mymovies.utils.NetworkUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRemoteSourceImpl@Inject constructor(private val apiEndPoint: ApiEndPoint, @ApplicationContext private val context: Context
): MovieRemoteSource {
    private val currentLocale: String = LocaleList.getDefault().get(0).language

    override suspend fun getTodayTrendingMovie(pageNumber: Int): MoviesResponse {
        return try { apiEndPoint.getTrendingMovies(pageNumber,currentLocale)
        } catch (exception: Exception) {
            if(NetworkUtils.isNetworkConnected(context)) {
                throw ErrorState.UnknownError
            } else {
                throw ErrorState.InternetConnectionError
            }
        }
    }

    override suspend fun getMovieDetail(movieId: Int): MovieDetailResponse {
        return try { apiEndPoint.getMovieDetail(movieId, currentLocale)
        } catch (exception: Exception) {
            if(NetworkUtils.isNetworkConnected(context)) {
                throw ErrorState.UnknownError
            } else {
                throw ErrorState.InternetConnectionError
            }
        }
    }

    override suspend fun searchMovie(query: String, pageNumber: Int): MoviesResponse {
        return try { apiEndPoint.searchMovies(pageNumber,query,currentLocale)
        } catch (exception: Exception) {
            if(NetworkUtils.isNetworkConnected(context)) {
                throw ErrorState.UnknownError
            } else {
                throw ErrorState.InternetConnectionError
            }
        }
    }
}