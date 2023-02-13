package levilin.moviedatabase.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import levilin.moviedatabase.data.remote.RemoteRepository
import levilin.moviedatabase.model.remote.Movies
import levilin.moviedatabase.utility.ConstantValue
import levilin.moviedatabase.utility.NetworkResult
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val remoteRepository: RemoteRepository, application: Application): AndroidViewModel(application) {
    // API response
    var movieResultResponse: MutableLiveData<NetworkResult<Movies>> = MutableLiveData()
    var responseText: String = "Test"

    init {
        getMoviesInfo(query = "a")

//        // initialize arranged list to local database
//        if (_allLocalItems.value.isEmpty()) {
//            insertCurrencyItemDatabase()
//        }
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
            getMoviesList(queries = provideSearchQueries(query = query))
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

                responseText = movieResultResponse.value!!.data!!.movieResults.toString()
                Log.d("TAG", "Response Body: ${movieResultResponse.value!!.data!!.movieResults}")

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
                val currencyExchangeRayeResponse = response.body()
                NetworkResult.Success(data = currencyExchangeRayeResponse!!)
            }
            else -> {
                NetworkResult.Error(message = response.message().toString())
            }
        }
    }

    private fun provideSearchQueries(query: String): Map<String, String> {
        val queries = HashMap<String, String>()
        queries.apply {
            this["api_key"] = ConstantValue.API_KEY_V3
            this["query"] = query
        }
        return queries
    }

    fun searchMovie(query: String) {

    }
}
