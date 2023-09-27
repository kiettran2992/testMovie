package com.example.mymovies.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymovies.data.model.ErrorState
import com.example.mymovies.data.model.db.MovieDetail
import com.example.mymovies.data.repository.RepoResponse
import com.example.mymovies.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<MovieDetailUIState>(MovieDetailUIState.Loading)
    var uiState: StateFlow<MovieDetailUIState> = _uiState

    fun fetchMovieDetail(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = MovieDetailUIState.Loading

            when(val result = movieRepository.getMovieDetail(movieId = movieId)) {
                is RepoResponse.Success -> {
                    result.data.let {
                        _uiState.value = MovieDetailUIState.Success(it)
                    }
                }
                is RepoResponse.Error -> {
                    result.error.let {
                        _uiState.value = MovieDetailUIState.Error(it)
                    }
                }
            }
        }
    }

    sealed class MovieDetailUIState{
        object Loading : MovieDetailUIState()
        class Error(val errorState: ErrorState) : MovieDetailUIState()
        class Success(val data: MovieDetail) : MovieDetailUIState()
    }
}

