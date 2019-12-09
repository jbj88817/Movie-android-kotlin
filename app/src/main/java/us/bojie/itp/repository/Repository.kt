package us.bojie.itp.repository

import androidx.lifecycle.LiveData
import us.bojie.itp.api.MyRetrofitBuilder
import us.bojie.itp.api.MyRetrofitBuilder.API_KEY
import us.bojie.itp.api.MyRetrofitBuilder.SEARCH_DEFAULT_STR
import us.bojie.itp.model.MovieResponse
import us.bojie.itp.ui.main.state.MainViewState
import us.bojie.itp.util.ApiSuccessResponse
import us.bojie.itp.util.DataState
import us.bojie.itp.util.GenericApiResponse


object Repository {

    fun getMovies(searchStr: String = SEARCH_DEFAULT_STR): LiveData<DataState<MainViewState>> {
        return object : NetworkBoundResource<MovieResponse, MainViewState>() {
            override fun handleApiSuccessResponse(response: ApiSuccessResponse<MovieResponse>) {
                result.value = DataState.data(
                    null,
                    MainViewState(
                        movieResponse = response.body
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<MovieResponse>> {
                return MyRetrofitBuilder.apiService.getMovies(API_KEY, searchStr)
            }

        }.asLiveData()
    }

}




























