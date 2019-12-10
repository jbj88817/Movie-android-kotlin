package us.bojie.itp.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class MovieResponse(
    val Response: String,
    @Expose
    @SerializedName("Search")
    val Movies: List<Movie>,
    val totalResults: String
)

@Parcelize
data class Movie(
    val Poster: String,
    val Title: String,
    val Type: String,
    val Year: String,
    val imdbID: String
) : Parcelable