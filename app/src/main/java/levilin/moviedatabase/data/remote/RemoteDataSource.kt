package levilin.moviedatabase.data.remote

import levilin.moviedatabase.model.remote.Movies
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val moviesAPI: MoviesAPI) {
    // Get Movies
    suspend fun getMovies(queries: Map<String, String>): Response<Movies> {
        return moviesAPI.getMovies(queries = queries)
    }
}