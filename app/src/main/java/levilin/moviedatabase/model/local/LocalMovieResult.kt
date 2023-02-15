package levilin.moviedatabase.model.local

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class LocalMovieResult(
    val adult: Boolean = false,
    val backdropPath: String = "",
    @PrimaryKey
    @ColumnInfo(name = "ID")
    val id: Int = 0,
    val originalLanguage: String = "",
    val originalTitle: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val posterPath: String = "",
    val releaseDate: String = "",
    val title: String = "",
    val video: Boolean = true,
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0
)