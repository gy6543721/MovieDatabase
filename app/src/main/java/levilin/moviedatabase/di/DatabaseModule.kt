package levilin.moviedatabase.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import levilin.moviedatabase.data.local.LocalDataSource
import levilin.moviedatabase.data.local.LocalDataSourceDAO
import levilin.moviedatabase.model.list.MovieResult
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = LocalDataSource.getInstance(context = context)

    @Singleton
    @Provides
    fun provideDAO(localDataSource: LocalDataSource): LocalDataSourceDAO = localDataSource.getDAO()

    @Singleton
    @Provides
    fun provideEntity() = MovieResult()
}