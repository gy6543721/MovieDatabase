package levilin.moviedatabase.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import levilin.moviedatabase.model.list.MovieResult
import levilin.moviedatabase.utility.ConstantValue

@Database(entities = [MovieResult::class], version = 1, exportSchema = false)
abstract class LocalDataSource : RoomDatabase() {
    abstract fun localDataSourceDAO(): LocalDataSourceDAO

    companion object {
        @Volatile
        private lateinit var instance: LocalDataSource

        fun getInstance(context: Context): LocalDataSource {
            if (!::instance.isInitialized) {
                instance = Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = LocalDataSource::class.java,
                    name = ConstantValue.DATABASE_FILE_NAME
                )
                    .fallbackToDestructiveMigration(dropAllTables = false)
                    .build()
            }
            return instance
        }
    }

    fun getDAO(): LocalDataSourceDAO {
        return instance.localDataSourceDAO()
    }
}