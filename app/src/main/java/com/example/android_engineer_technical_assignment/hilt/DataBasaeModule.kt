package com.example.android_engineer_technical_assignment.hilt

import android.content.Context
import androidx.room.Room
import com.example.android_engineer_technical_assignment.data.AppDatabase
import com.example.android_engineer_technical_assignment.data.Constant
import com.example.android_engineer_technical_assignment.data.MovieDao
import com.example.android_engineer_technical_assignment.data.MovieService
import com.example.android_engineer_technical_assignment.repository.MovieRepository
import com.example.android_engineer_technical_assignment.repository.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MovieModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

    companion object {
        @Provides
        @Singleton
        fun createAppDatabase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "favorites-db"
            ).build()
        }

        @Provides
        fun provideMovieDao(appDatabase: AppDatabase): MovieDao {
            return appDatabase.movieDao()
        }

        @Provides
        @Singleton
        fun provideMovieService(): MovieService {
            return Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieService::class.java)
        }
    }
}
