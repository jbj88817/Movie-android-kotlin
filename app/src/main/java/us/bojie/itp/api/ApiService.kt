package us.bojie.itp.api

import androidx.lifecycle.LiveData
import retrofit2.http.GET
import retrofit2.http.Query
import us.bojie.itp.model.MovieResponse
import us.bojie.itp.util.GenericApiResponse

interface ApiService {

    @GET("/")
    fun getMovies(@Query("apikey") apiKey: String, @Query("s") search: String)
            : LiveData<GenericApiResponse<MovieResponse>>
}