package com.example.mymovies.data.model

sealed class ErrorState: Exception() {
    object UnknownError: ErrorState()
    object InternetConnectionError: ErrorState()
}