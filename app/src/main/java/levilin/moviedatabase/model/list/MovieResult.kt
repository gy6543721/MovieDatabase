package levilin.moviedatabase.model.list

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import levilin.moviedatabase.utility.ConstantValue

@Entity(tableName = ConstantValue.DATABASE_TABLE_NAME)
data class MovieResult(
    @SerializedName("adult")
    val adult: Boolean = false,
    @PrimaryKey
    @ColumnInfo(name = "ID")
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("original_language")
    val originalLanguage: String = "",
    @SerializedName("original_title")
    val originalTitle: String = "",
    @SerializedName("overview")
    val overview: String = "",
    @SerializedName("popularity")
    val popularity: Double = 0.0,
    @SerializedName("poster_path")
    val posterPath: String = "",
    @SerializedName("release_date")
    val releaseDate: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("video")
    val video: Boolean = true,
    @SerializedName("vote_average")
    val voteAverage: Double = 0.0,
    @SerializedName("vote_count")
    val voteCount: Long = 0
)