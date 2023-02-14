package levilin.moviedatabase.data.remote

import levilin.moviedatabase.model.remote.detail.MovieDetail
import levilin.moviedatabase.model.remote.list.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface MoviesAPI {
    // Get Movies
    @GET("search/movie")
    suspend fun getMovies(@QueryMap queries: Map<String, String>): Response<Movies>

    // Get Movie Detail
    @GET("/movie/{id}")
    suspend fun getMovieDetail(@Path("id") id: String ,@QueryMap queries: Map<String, String>): Response<MovieDetail>
}