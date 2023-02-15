package levilin.moviedatabase.data.local

import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import levilin.moviedatabase.model.remote.list.MovieResult
import javax.inject.Inject

@ViewModelScoped
class LocalRepository @Inject constructor(private val localDataSourceDAO: LocalDataSourceDAO) {

    val getAllItems: Flow<List<MovieResult>> = localDataSourceDAO.getAllItems()

    suspend fun insertItem(movieResult: MovieResult) {
        localDataSourceDAO.insertItem(movieResult = movieResult)
    }
    suspend fun updateItem(movieResult: MovieResult) {
        localDataSourceDAO.updateItem(movieResult = movieResult)
    }
    suspend fun deleteItem(movieResult: MovieResult) {
        localDataSourceDAO.deleteItem(movieResult = movieResult)
    }

}