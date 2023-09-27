package com.example.mymovies.di

import android.content.Context
import androidx.room.Room
import com.example.mymovies.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun provideAppDatabase(
        @ApplicationContext app: Context
    ): AppDatabase {
        var dbInstance: AppDatabase? = null
        dbInstance = Room.databaseBuilder(
                app,
        AppDatabase::class.java,
        "app_db"
        ).build()

        return dbInstance
    }

   // The reason we can construct a database for the repo

    @Singleton
    @Provides
    fun provideMovieDao(db: AppDatabase) = db.getMovieDAO()

    @Singleton
    @Provides
    fun provideMovieDetailDao(db: AppDatabase) = db.getMovieDetailDAO()

    @Singleton
    @Provides
    fun provideRemoteKeysDap(db: AppDatabase) = db.getRemoteKeysDAO()
}