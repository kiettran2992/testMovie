package com.example.mymovies.data.model.api.mapper

import com.example.mymovies.data.model.api.GenresResponse
import com.example.mymovies.data.model.api.MovieDetailResponse
import com.example.mymovies.data.model.api.MovieResponse
import com.example.mymovies.data.model.api.ProductionCompaniesResponse
import com.example.mymovies.data.model.db.Genres
import com.example.mymovies.data.model.db.Movie
import com.example.mymovies.data.model.db.MovieDetail
import com.example.mymovies.data.model.db.ProductionCompany

fun List<MovieResponse>.toMovies(): List<Movie> {
    val result = arrayListOf<Movie>()
    this.forEach {
        result.add(Movie(
            id = it.id,
            posterPath = it.posterPath,
            releaseDate = it.releaseDate,
            voteAverage = it.voteAverage,
            page = it.page,
            title = it.title
        ))
    }

    return result
}

fun MovieDetailResponse.toMovieDetail(): MovieDetail {
    return MovieDetail(
        id = this.id,
        originalLanguage = this.originalLanguage,
        posterPath = this.posterPath,
        originalTitle = this.originalTitle,
        overview = this.overview,
        releaseDate = this.releaseDate,
        revenue = this.revenue,
        runTime = this.runtime,
        status = this.status,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        genres = this.genres?.toGenres(),
        homePage = this.homepage,
        backdropPath = this.backdropPath,
        productionCompanies = this.productionCompanies?.toProductionCompanies()
    )
}

fun List<GenresResponse>.toGenres(): List<Genres> {
    val result = arrayListOf<Genres>()
    this.forEach {
        result.add(Genres(id = it.id, name = it.name))
    }

    return result
}

fun List<ProductionCompaniesResponse>.toProductionCompanies(): List<ProductionCompany> {
    val result = arrayListOf<ProductionCompany>()
    this.forEach {
        result.add(
            ProductionCompany(
                id = it.id,
                name = it.name,
                logoPath = it.logoPath,
                originCountry = it.originCountry
            )
        )
    }

    return result
}