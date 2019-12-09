package us.bojie.itp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import us.bojie.itp.util.LiveDataCallAdapterFactory

object MyRetrofitBuilder {
    const val BASE_URL = "http://www.omdbapi.com/"
    const val SEARCH_DEFAULT_STR = "avengers"
    const val API_KEY = "586573af"

    val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiService: ApiService by lazy {
        retrofitBuilder.build().create(ApiService::class.java)
    }
}