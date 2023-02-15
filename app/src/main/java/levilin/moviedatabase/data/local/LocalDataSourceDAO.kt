package levilin.moviedatabase.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import levilin.moviedatabase.model.remote.list.MovieResult

@Dao
interface LocalDataSourceDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(movieResult: MovieResult)

    @Query("SELECT * FROM LOCAL_DATA_LIST")
    fun getAllItems(): Flow<List<MovieResult>>

    @Update
    suspend fun updateItem(movieResult: MovieResult)

    @Delete
    suspend fun deleteItem(movieResult: MovieResult)
}