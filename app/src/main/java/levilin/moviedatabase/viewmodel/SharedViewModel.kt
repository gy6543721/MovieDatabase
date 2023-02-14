package levilin.moviedatabase.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import levilin.moviedatabase.data.remote.RemoteRepository
import levilin.moviedatabase.model.remote.MovieResult
import levilin.moviedatabase.model.remote.Movies
import levilin.moviedatabase.utility.ConstantValue
import levilin.moviedatabase.utility.NetworkResult
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val remoteRepository: RemoteRepository, application: Application): AndroidViewModel(application) {
    // API response
    private var movieResultResponse: MutableLiveData<NetworkResult<Movies>> = MutableLiveData()

    var searchQuery = mutableStateOf(value = ConstantValue.DEFAULT_QUERY)

    var currentPage = mutableStateOf(value = 1)
    var totalPage = mutableStateOf(value = Int.MAX_VALUE)

    var moviesList = mutableStateOf<List<MovieResult>>(listOf())
    var loadingError = mutableStateOf("")
    var isLoading = mutableStateOf(true)

    init {
        loadMovies()
    }

    fun loadMovies() {
        isLoading.value = true
        getMoviesInfo(query = searchQuery.value)
    }

    private fun checkInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapability = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    private fun getMoviesInfo(query: String) {
        viewModelScope.launch {
            getMoviesList(queries = provideSearchQueries(query = query, page = currentPage.value))
        }
    }

    private fun getMoviesList(queries: Map<String, String>) {
        viewModelScope.launch {
            getMoviesListSafeCall(queries = queries)
        }
    }

    private suspend fun getMoviesListSafeCall(queries: Map<String, String>) {
        if (checkInternetConnection()) {
            try {
                val response = remoteRepository.remoteDataSource.getMovies(queries = queries)
                Log.d("TAG", "getMoviesListSafeCall Response: ${response.code()}")
                movieResultResponse.value = handleMoviesListResponse(response = response)
                isLoading.value = false

                currentPage.value = movieResultResponse.value!!.data!!.page
                totalPage.value = movieResultResponse.value!!.data!!.totalPages
                Log.d("TAG", "Response Body Page: ${movieResultResponse.value!!.data!!.page}")

                Log.d("TAG", "Response Body: ${movieResultResponse.value!!.data!!.movieResults}")
                moviesList.value = movieResultResponse.value!!.data!!.movieResults

            } catch (e: Exception) {
                movieResultResponse.value = NetworkResult.Error(message = e.localizedMessage)
            }
        } else {
            movieResultResponse.value = NetworkResult.Error(message = "No Internet Connection")
        }
    }

    private fun handleMoviesListResponse(response: Response<Movies>): NetworkResult<Movies> {
        return when {
            response.message().toString().contains("timeout") -> {
                NetworkResult.Error(message = "Time Out")
            }
            response.isSuccessful -> {
                val moviesListResponse = response.body()
                NetworkResult.Success(data = moviesListResponse!!)
            }
            else -> {
                NetworkResult.Error(message = response.message().toString())
            }
        }
    }

    private fun provideSearchQueries(query: String, page: Int): Map<String, String> {
        val queries = HashMap<String, String>()
        queries.apply {
            this["api_key"] = ConstantValue.API_KEY_V3
            this["query"] = query
            this["page"] = page.toString()
        }
        return queries
    }
}
