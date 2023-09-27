package com.example.mymovies.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mymovies.data.model.db.MovieDetail

@Dao
interface MovieDetailDao {

    @Query("SELECT * FROM movie_detail WHERE id=:id")
    suspend fun getMovieDetail(id: Int): MovieDetail?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetail(movieDetail: MovieDetail)
}