package us.bojie.itp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val Response: String,
    @Expose
    @SerializedName("Search")
    val Movies: List<Movie>,
    val totalResults: String
)

data class Movie(
    val Poster: String,
    val Title: String,
    val Type: String,
    val Year: String,
    val imdbID: String
)