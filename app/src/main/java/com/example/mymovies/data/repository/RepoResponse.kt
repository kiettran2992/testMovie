package com.example.mymovies.data.repository

import com.example.mymovies.data.model.ErrorState

sealed class RepoResponse<T> {
    data class Success<T>(val data: T) : RepoResponse<T>()
    data class Error<T>(val error: ErrorState) : RepoResponse<T>()
}