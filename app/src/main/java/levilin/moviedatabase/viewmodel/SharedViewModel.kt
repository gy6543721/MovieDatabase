package levilin.moviedatabase.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
    var displayQuery = mutableStateOf(value = "")
    var currentPage = mutableStateOf(value = 1)
    var totalPage = mutableStateOf(value = Int.MAX_VALUE)
    
    var errorMovieListMessage = mutableStateOf(value = "")
    var isMovieListLoading = mutableStateOf(value = true)

    var errorMovieDetailMessage = mutableStateOf(value = "")
    var isMovieDetailLoading = mutableStateOf(value = true)

    var favoriteList = mutableStateOf<List<MovieResult>>(listOf())
    var movieList = mutableStateOf<List<MovieResult>>(listOf())
    var movieDetail = mutableStateOf(MovieDetail())

    init {
        getAllItems()
        loadMovieList()
    }

    fun loadMovieList() {
        isMovieListLoading.value = true
        getMovieList(queries = provideMovieListQueries(query = searchQuery.value, page = currentPage.value))
//        updateMovieList(query = searchQuery.value)
    }

    fun loadFavoriteList() {
        getAllItems()
    }

    fun loadMovieDetail(id: String) {
        isMovieDetailLoading.value = true
        getMovieDetail(id = id, queries = provideMovieDetailQueries())
//        updateMovieDetail(id = id)
    }

    fun moveCurrentPage(value: Int) {
        val targetPage = currentPage.value + value
        if (targetPage < 1 || targetPage > totalPage.value) {
            return
        } else {
            currentPage.value = targetPage
            loadMovieList()
        }
    }

    // Local database action
    private fun getAllItems() {
        viewModelScope.launch {
            localRepository.getAllItems.collect { itemList ->
                favoriteList.value = itemList
            }
        }
    }

    private fun insertItem(movieResult: MovieResult) {
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.insertItem(movieResult = movieResult)
//            Log.d("TAG","add to database: ${movieResult.id}")
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
//            Log.d("TAG","remove from database: ${movieResult.id}")
        }
    }

    // Add favorite action
    fun favoriteAction(isFavorite: Boolean, entry: MovieResult) {
        if (!isFavorite && checkFavorite(input = entry)) {
//            Log.d("TAG","remove favorite: ${entry.id}")
            // Delete from local database
            deleteItem(movieResult = entry)
        } else {
            if (isFavorite && !checkFavorite(input = entry)) {
//                Log.d("TAG","add favorite: ${entry.id}")
                // Insert to local database
                insertItem(movieResult = entry)
            } else {
                return
            }
        }
    }

    fun checkFavorite(input: MovieResult): Boolean {
        for (i in favoriteList.value.indices) {
            if (favoriteList.value[i].id == input.id) {
                return true
            }
        }
        return false
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
    private fun updateMovieList(query: String) {
        viewModelScope.launch {
            getMovieList(queries = provideMovieListQueries(query = query, page = currentPage.value))
        }
    }

    private fun getMovieList(queries: Map<String, String>) {
        viewModelScope.launch {
            getMovieListSafeCall(queries = queries)
        }
    }

    private suspend fun getMovieListSafeCall(queries: Map<String, String>) {
        if (checkInternetConnection()) {
            try {
                val response = remoteRepository.remoteDataSource.getMovies(queries = queries)
//                Log.d("TAG", "getMoviesListSafeCall Response: ${response.code()}")
                movieInfoListResponse.value = handleMovieListResponse(response = response)
                totalPage.value = movieInfoListResponse.value!!.data!!.totalPages
                if (currentPage.value > totalPage.value) {
                    currentPage.value = 1
                    loadMovieList()
                }
                movieList.value = movieInfoListResponse.value!!.data!!.movieResults
                errorMovieListMessage.value = ""
            } catch (e: Exception) {
                movieInfoListResponse.value = NetworkResult.Error(message = e.localizedMessage)
                errorMovieListMessage.value = movieInfoListResponse.value!!.message.toString()
//                Log.d("TAG", "error : ${movieInfoListResponse.value!!.message}")
            }
        } else {
            movieInfoListResponse.value = NetworkResult.Error(message = "No Internet Connection")
            errorMovieListMessage.value = movieInfoListResponse.value!!.message.toString()
        }
        isMovieListLoading.value = false
    }

    private fun handleMovieListResponse(response: Response<MovieInfo>): NetworkResult<MovieInfo> {
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

    private fun provideMovieListQueries(query: String, page: Int): Map<String, String> {
        val queries = HashMap<String, String>()
        queries.apply {
            this["api_key"] = ConstantValue.API_KEY_V3
            this["query"] = query
            this["page"] = page.toString()
        }
        return queries
    }

    // For MovieDetail Page
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
//                Log.d("TAG", "getMovieDetailSafeCall Response: ${response.code()}")
                movieDetailResponse.value = handleMovieDetailResponse(response = response)
                movieDetail.value = movieDetailResponse.value!!.data!!
                errorMovieDetailMessage.value = ""
            } catch (e: Exception) {
                movieDetailResponse.value = NetworkResult.Error(message = e.localizedMessage)
                errorMovieDetailMessage.value = movieDetailResponse.value!!.message.toString()
//                Log.d("TAG", "error : ${movieDetailResponse.value!!.message}")
            }
        } else {
            movieDetailResponse.value = NetworkResult.Error(message = "No Internet Connection")
            errorMovieDetailMessage.value = movieDetailResponse.value!!.message.toString()
        }

        if (id == movieDetail.value.id.toString()) {
            isMovieDetailLoading.value = false
        } else {
            getMovieDetail(id = id, queries = provideMovieDetailQueries())
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
