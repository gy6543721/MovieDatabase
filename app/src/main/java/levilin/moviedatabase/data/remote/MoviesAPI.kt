package levilin.moviedatabase.data.remote

import levilin.moviedatabase.model.remote.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface MoviesAPI {
    // Get Search Movies
    @GET("search/movie")
    suspend fun getMovies(@QueryMap queries: Map<String, String>): Response<Movies>
}