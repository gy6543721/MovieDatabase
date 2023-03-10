package levilin.moviedatabase.data.remote

import levilin.moviedatabase.model.detail.MovieDetail
import levilin.moviedatabase.model.list.MovieInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface MoviesAPI {
    // Get MovieInfo
    @GET("search/movie")
    suspend fun getMovies(@QueryMap queries: Map<String, String>): Response<MovieInfo>

    // Get MovieDetail
    @GET("movie/{id}")
    suspend fun getMovieDetail(@Path("id") id: String ,@QueryMap queries: Map<String, String>): Response<MovieDetail>
}