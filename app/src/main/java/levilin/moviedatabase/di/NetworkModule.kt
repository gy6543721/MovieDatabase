package levilin.moviedatabase.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import levilin.moviedatabase.data.remote.MoviesAPI
import levilin.moviedatabase.utility.ConstantValue
import levilin.moviedatabase.utility.NullStringToEmptyAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl() = ConstantValue.BASE_URL

    @Singleton
    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient, BASE_URL:String): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().registerTypeAdapterFactory(NullStringToEmptyAdapterFactory<String>()).create()))
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create(GsonBuilder().registerTypeAdapterFactory(NullStringToEmptyAdapterFactory<String>()).create())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideAPI(retrofit: Retrofit): MoviesAPI {
        return retrofit.create(MoviesAPI::class.java)
    }
}