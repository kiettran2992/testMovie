package com.example.mymovies.di

import com.example.mymovies.data.repository.MovieRepositoryImpl
import com.example.mymovies.data.repository.datasource.local.MovieLocalSource
import com.example.mymovies.data.repository.datasource.local.MovieLocalSourceImpl
import com.example.mymovies.data.repository.datasource.remote.MovieRemoteSource
import com.example.mymovies.data.repository.datasource.remote.MovieRemoteSourceImpl
import com.example.mymovies.domain.GetMovieUseCase
import com.example.mymovies.domain.IGetMovieUseCase
import com.example.mymovies.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideGetMovieUseCase(movieUseCase: GetMovieUseCase): IGetMovieUseCase

    @Binds
    fun provideMovieRepository(movieRepository: MovieRepositoryImpl): MovieRepository

    @Binds
    fun provideMovieLocalDataSource(movieLocalSource: MovieLocalSourceImpl): MovieLocalSource

    @Binds
    fun provideMovieRemoteDatSource(movieRemoteSource: MovieRemoteSourceImpl): MovieRemoteSource
}