package com.example.android_engineer_technical_assignment.hilt


import android.content.Context
import androidx.room.Room
import com.example.android_engineer_technical_assignment.data.AppDatabase
import com.example.android_engineer_technical_assignment.data.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieModule {


    @Provides
    @Singleton
    fun createAppDatabase(@ApplicationContext context: Context): AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "favorites-db").build()
    }

    @Provides
    fun provideMovieDao(appDatabase: AppDatabase): MovieDao{
        return appDatabase.movieDao()
    }


}