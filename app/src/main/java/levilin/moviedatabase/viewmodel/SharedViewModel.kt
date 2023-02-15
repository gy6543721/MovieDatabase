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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import levilin.moviedatabase.data.local.LocalRepository
import levilin.moviedatabase.data.remote.RemoteRepository
import levilin.moviedatabase.model.detail.MovieDetail
import levilin.moviedatabase.model.list.MovieResult
import levilin.moviedatabase.model.list.MovieInfo
import levilin.moviedatabase.utility.ConstantValue
import levilin.moviedatabase.utility.NetworkResult
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val remoteRepository: RemoteRepository, private val localRepository: LocalRepository, application: Application): AndroidViewModel(application) {

    // API response
    private var movieInfoListResponse: MutableLiveData<NetworkResult<MovieInfo>> = MutableLiveData()
    private var movieDetailResponse: MutableLiveData<NetworkResult<MovieDetail>> = MutableLiveData()

    var searchQuery = mutableStateOf(value = ConstantValue.DEFAULT_QUERY)
        private set
    var loadingError = mutableStateOf("")
    var isRemoteLoading = mutableStateOf(true)

    var currentPage = mutableStateOf(value = 1)
    var totalPage = mutableStateOf(value = Int.MAX_VALUE)

    var favoriteList = mutableStateOf<ArrayList<MovieResult>>(arrayListOf())
    var movieList = mutableStateOf<List<MovieResult>>(listOf())
    var movieDetail = mutableStateOf(MovieDetail())

    init {
        getAllItems()
        loadMoviesList()
    }

    fun loadMoviesList() {
        isRemoteLoading.value = true
        updateMoviesList(query = searchQuery.value)
    }

    fun loadFavoriteList() {
        getAllItems()
    }

    fun loadMovieDetail(id: String) {
        isRemoteLoading.value = true
        updateMovieDetail(id = id)
    }


    private fun getAllItems() {
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.getAllItems.collect() { itemList ->
                favoriteList.value = itemList as ArrayList<MovieResult>
            }
        }
    }

    private fun insertItem(movieResult: MovieResult) {
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.insertItem(movieResult = movieResult)
            Log.d("TAG","add to database: ${movieResult.id}")
        }
    }

    private fun updateItem(movieResult: MovieResult) {
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.updateItem(movieResult = movieResult)
        }
    }

    private fun deleteItem(movieResult: MovieResult) {
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.deleteItem(movieResult = movieResult)
            Log.d("TAG","remove from database: ${movieResult.id}")
        }
    }

    fun favoriteAction(isFavorite: Boolean, entry: MovieResult) {
        if (!isFavorite && checkFavorite(input = entry)) {
            favoriteList.value.remove(element = entry)
            Log.d("TAG","remove favorite: ${entry.id}")
            // Delete from local database
            deleteItem(movieResult = entry)
        } else {
            if (isFavorite && !checkFavorite(input = entry)) {
                favoriteList.value.add(element = entry)
                Log.d("TAG","add favorite: ${entry.id}")
                // Insert to local database
                insertItem(movieResult = entry)
            } else {
                return
            }
        }
    }

    private fun checkFavorite(input: MovieResult): Boolean {
        return favoriteList.value.contains(input)
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

    // For MovieInfo List
    private fun updateMoviesList(query: String) {
        viewModelScope.launch {
            getMoviesList(queries = provideMoviesListQueries(query = query, page = currentPage.value))
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
                movieInfoListResponse.value = handleMoviesListResponse(response = response)
                isRemoteLoading.value = false

                currentPage.value = movieInfoListResponse.value!!.data!!.page
                totalPage.value = movieInfoListResponse.value!!.data!!.totalPages
//                Log.d("TAG", "MovieList Response Body Page: ${movieInfoListResponse.value!!.data!!.page}")
//                Log.d("TAG", "MovieList Response Body: ${movieInfoListResponse.value!!.data!!.movieResults}")
                movieList.value = movieInfoListResponse.value!!.data!!.movieResults

            } catch (e: Exception) {
                movieInfoListResponse.value = NetworkResult.Error(message = e.localizedMessage)
            }
        } else {
            movieInfoListResponse.value = NetworkResult.Error(message = "No Internet Connection")
        }
    }

    private fun handleMoviesListResponse(response: Response<MovieInfo>): NetworkResult<MovieInfo> {
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

    private fun provideMoviesListQueries(query: String, page: Int): Map<String, String> {
        val queries = HashMap<String, String>()
        queries.apply {
            this["api_key"] = ConstantValue.API_KEY_V3
            this["query"] = query
            this["page"] = page.toString()
        }
        return queries
    }

    // For Movie Detail
    private fun updateMovieDetail(id: String) {
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
        if (checkInternetConnection()) {
            try {
                val response = remoteRepository.remoteDataSource.getMovieDetail(id = id, queries = queries)
                Log.d("TAG", "getMovieDetailSafeCall Response: ${response.code()}")
                movieDetailResponse.value = handleMovieDetailResponse(response = response)
                isRemoteLoading.value = false
//                Log.d("TAG", "MovieDetail Response Body ID: ${movieDetailResponse.value!!.data!!.id}")
//                Log.d("TAG", "MovieDetail Response Body: ${movieDetailResponse.value!!.data!!}")
                movieDetail.value = movieDetailResponse.value!!.data!!

            } catch (e: Exception) {
                movieDetailResponse.value = NetworkResult.Error(message = e.localizedMessage)
            }
        } else {
            movieDetailResponse.value = NetworkResult.Error(message = "No Internet Connection")
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
