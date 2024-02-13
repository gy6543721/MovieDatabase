package levilin.moviedatabase.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.runtime.mutableIntStateOf
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
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MovieDatabaseViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
    application: Application
) : AndroidViewModel(application) {
    // API response
    private var movieInfoListResponse: MutableLiveData<NetworkResult<MovieInfo>> = MutableLiveData()
    private var movieDetailResponse: MutableLiveData<NetworkResult<MovieDetail>> = MutableLiveData()

    var searchQuery = mutableStateOf(value = ConstantValue.DEFAULT_QUERY)
    var displayQuery = mutableStateOf(value = "")
    var currentPage = mutableIntStateOf(value = 1)
    var totalPage = mutableIntStateOf(value = Int.MAX_VALUE)

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
        getMovieList(
            queries = provideMovieListQueries(
                query = searchQuery.value,
                page = currentPage.intValue
            )
        )
    }

    fun loadFavoriteList() {
        getAllItems()
    }

    fun loadMovieDetail(id: String) {
        isMovieDetailLoading.value = true
        getMovieDetail(id = id, queries = provideMovieDetailQueries())
    }

    fun moveCurrentPage(value: Int) {
        val targetPage = currentPage.intValue + value
        if (targetPage < 1 || targetPage > totalPage.intValue) {
            return
        } else {
            currentPage.intValue = targetPage
        }
        if (currentPage.intValue != movieInfoListResponse.value?.data?.page) {
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
        }
    }

    // Add favorite action
    fun favoriteAction(isFavorite: Boolean, entry: MovieResult) {
        if (!isFavorite && checkFavorite(input = entry)) {
            // Delete from local database
            deleteItem(movieResult = entry)
        } else {
            if (isFavorite && !checkFavorite(input = entry)) {
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
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapability =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    // For movie info list
    private fun getMovieList(queries: Map<String, String>) {
        viewModelScope.launch {
            getMovieListSafeCall(queries = queries)
        }
    }

    private suspend fun getMovieListSafeCall(queries: Map<String, String>) {
        if (checkInternetConnection()) {
            try {
                val response = remoteRepository.remoteDataSource.getMovieList(queries = queries)
                movieInfoListResponse.value = handleMovieListResponse(response = response)
                totalPage.intValue = movieInfoListResponse.value!!.data!!.totalPages
                if (currentPage.intValue > totalPage.intValue) {
                    currentPage.intValue = 1
                    getMovieList(
                        queries = provideMovieListQueries(
                            query = searchQuery.value,
                            page = currentPage.intValue
                        )
                    )
                }
                if (currentPage.intValue != movieInfoListResponse.value!!.data!!.page) {
                    return
                }
                movieList.value = movieInfoListResponse.value!!.data!!.movieResults
                errorMovieListMessage.value = ""
            } catch (e: Exception) {
                movieInfoListResponse.value = NetworkResult.Error(message = e.localizedMessage)
                errorMovieListMessage.value = movieInfoListResponse.value!!.message.toString()
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
            this["language"] = Locale.getDefault().toLanguageTag()
        }
        return queries
    }

    // For movie detail page
    private fun getMovieDetail(id: String, queries: Map<String, String>) {
        viewModelScope.launch {
            getMovieDetailSafeCall(id = id, queries = queries)
        }
    }

    private suspend fun getMovieDetailSafeCall(id: String, queries: Map<String, String>) {
        if (checkInternetConnection()) {
            try {
                val response =
                    remoteRepository.remoteDataSource.getMovieDetail(id = id, queries = queries)
                movieDetailResponse.value = handleMovieDetailResponse(response = response)
                movieDetail.value = movieDetailResponse.value!!.data!!
                if (id != movieDetailResponse.value?.data?.id.toString()) {
                    getMovieDetail(id = id, queries = provideMovieDetailQueries())
                }
                errorMovieDetailMessage.value = ""
            } catch (e: Exception) {
                movieDetailResponse.value = NetworkResult.Error(message = e.localizedMessage)
                errorMovieDetailMessage.value = movieDetailResponse.value!!.message.toString()
            }
        } else {
            movieDetailResponse.value = NetworkResult.Error(message = "No Internet Connection")
            errorMovieDetailMessage.value = movieDetailResponse.value!!.message.toString()
        }
        isMovieDetailLoading.value = false
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
            this["language"] = Locale.getDefault().toLanguageTag()
        }
        return queries
    }
}
