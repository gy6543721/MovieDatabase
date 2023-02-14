package levilin.moviedatabase.data.remote

import levilin.moviedatabase.model.remote.detail.MovieDetail
import levilin.moviedatabase.model.remote.list.Movies
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val moviesAPI: MoviesAPI) {
    // Get Movies
    suspend fun getMovies(queries: Map<String, String>): Response<Movies> {
        return moviesAPI.getMovies(queries = queries)
    }

    // Get Movie Detail
    suspend fun getMovieDetail(id: String, queries: Map<String, String>): Response<MovieDetail> {
        return moviesAPI.getMovieDetail(id = id, queries = queries)
    }

}