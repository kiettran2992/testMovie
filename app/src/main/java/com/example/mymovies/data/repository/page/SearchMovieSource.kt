package com.example.mymovies.data.repository.page

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mymovies.data.model.ErrorState
import com.example.mymovies.data.model.api.mapper.toMovies
import com.example.mymovies.data.model.db.Movie
import com.example.mymovies.data.repository.datasource.remote.MovieRemoteSource

class SearchMovieSource(
    private val movieRemoteSource: MovieRemoteSource,
    private val searchText: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val nextPage = params.key ?: 1
            val movieListResponse = movieRemoteSource.searchMovie(searchText, nextPage)

            LoadResult.Page(
                data = movieListResponse.results.toMovies(),
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = movieListResponse.page.plus(1)

            )
        } catch (e: ErrorState) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}