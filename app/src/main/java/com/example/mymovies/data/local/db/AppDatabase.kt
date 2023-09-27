package com.example.mymovies.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mymovies.data.local.db.dao.MovieDao
import com.example.mymovies.data.local.db.dao.MovieDetailDao
import com.example.mymovies.data.local.db.dao.RemoteKeysDao
import com.example.mymovies.data.model.db.Movie
import com.example.mymovies.data.model.db.MovieDetail
import com.example.mymovies.data.model.db.RemoteKeys
import com.example.mymovies.data.model.db.converters.MovieDetailConverter

@Database(
    entities = [Movie::class, MovieDetail::class, RemoteKeys::class], // Tell the database the entries will hold data of this type
    version = 1
)
@TypeConverters(MovieDetailConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMovieDAO(): MovieDao
    abstract fun getMovieDetailDAO(): MovieDetailDao
    abstract fun getRemoteKeysDAO(): RemoteKeysDao
}