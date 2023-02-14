package levilin.moviedatabase.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import levilin.moviedatabase.data.remote.RemoteRepository
import levilin.moviedatabase.model.remote.detail.MovieDetail
import levilin.moviedatabase.utility.ConstantValue
import levilin.moviedatabase.utility.NetworkResult
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val remoteRepository: RemoteRepository) : ViewModel() {

    var movieDetailResponse: MutableLiveData<NetworkResult<MovieDetail>> = MutableLiveData()

//    suspend fun getMovieDetail(id: String): Response<MovieDetail> {
//        return remoteRepository.remoteDataSource.getMovieDetail(id = id, queries = provideMovieDetailQueries())
//    }

    // For Movie Detail
    fun updateMovieDetail(id: String) {
        viewModelScope.launch {
            getMovieDetail(id = id, queries = provideMovieDetailQueries())
        }
    }

    private fun getMovieDetail(id: String, queries: Map<String, String>) {
        viewModelScope.launch {
            getMovieDetailSafeCall(id = id, queries = queries)
        }
    }

    private suspend fun getMovieDetailSafeCall(id: String, queries: Map<String, String>) {
        try {
            val response = remoteRepository.remoteDataSource.getMovieDetail(id = id, queries = queries)
            movieDetailResponse.value = handleMovieDetailResponse(response = response)

        } catch (e: Exception) {
            movieDetailResponse.value = NetworkResult.Error(message = e.localizedMessage)
        }
    }

    private fun handleMovieDetailResponse(response: Response<MovieDetail>): NetworkResult<MovieDetail> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error(message = "Time Out")
            }
            response.isSuccessful -> {
                val movieDetailResponse = response.body()
                NetworkResult.Success(data = movieDetailResponse!!)
            }
            else -> {
                NetworkResult.Error(message = response.message().toString())
            }
        }
    }

    private fun provideMovieDetailQueries(): Map<String, String> {
        val queries = HashMap<String, String>()
        queries.apply {
            this["api_key"] = ConstantValue.API_KEY_V3
        }
        return queries
    }
}