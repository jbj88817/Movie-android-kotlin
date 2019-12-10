package us.bojie.itp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import us.bojie.itp.model.Movie
import us.bojie.itp.model.MovieResponse
import us.bojie.itp.repository.Repository
import us.bojie.itp.ui.main.state.MainStateEvent
import us.bojie.itp.ui.main.state.MainStateEvent.GetMoviesEvent
import us.bojie.itp.ui.main.state.MainStateEvent.None
import us.bojie.itp.ui.main.state.MainViewState
import us.bojie.itp.util.AbsentLiveData
import us.bojie.itp.util.DataState

class MainViewModel : ViewModel() {
    private val _stateEvent: MutableLiveData<MainStateEvent> = MutableLiveData()
    private val _viewState: MutableLiveData<MainViewState> = MutableLiveData()

    val viewState: LiveData<MainViewState>
        get() = _viewState


    val dataState: LiveData<DataState<MainViewState>> = Transformations
        .switchMap(_stateEvent){stateEvent ->
            stateEvent?.let {
                handleStateEvent(stateEvent)
            }
        }

    private fun handleStateEvent(stateEvent: MainStateEvent): LiveData<DataState<MainViewState>> {
        println("DEBUG: New StateEvent detected: $stateEvent")
        return when(stateEvent){

            is GetMoviesEvent -> {
                Repository.getMovies()
            }

            is None ->{
                AbsentLiveData.create()
            }
        }
    }

    fun setMovieResponseData(movieResponse: MovieResponse?){
        val update = getCurrentViewStateOrNew()
        update.movieResponse = movieResponse
        _viewState.value = update
    }

    fun setMovie(movie: Movie){
        val update = getCurrentViewStateOrNew()
        update.movie = movie
        _viewState.value = update
    }

    fun getCurrentViewStateOrNew(): MainViewState {
        val value = viewState.value?.let{
            it
        }?: MainViewState()
        return value
    }

    fun setStateEvent(event: MainStateEvent){
        val state: MainStateEvent
        state = event
        _stateEvent.value = state
    }
}
