package levilin.moviedatabase.data.remote

import levilin.moviedatabase.model.detail.MovieDetail
import levilin.moviedatabase.model.list.MovieInfo
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val moviesAPI: MoviesAPI) {
    // Get MovieInfo
    suspend fun getMovies(queries: Map<String, String>): Response<MovieInfo> {
        return moviesAPI.getMovies(queries = queries)
    }

    // Get Movie Detail
    suspend fun getMovieDetail(id: String, queries: Map<String, String>): Response<MovieDetail> {
        return moviesAPI.getMovieDetail(id = id, queries = queries)
    }

}