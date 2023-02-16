package levilin.moviedatabase.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import levilin.moviedatabase.model.list.MovieResult
import levilin.moviedatabase.utility.ConstantValue

@Dao
interface LocalDataSourceDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(movieResult: MovieResult)

    @Query("SELECT * FROM ${ConstantValue.DATABASE_TABLE_NAME}")
    fun getAllItems(): Flow<List<MovieResult>>

    @Update
    suspend fun updateItem(movieResult: MovieResult)

    @Delete
    suspend fun deleteItem(movieResult: MovieResult)
}