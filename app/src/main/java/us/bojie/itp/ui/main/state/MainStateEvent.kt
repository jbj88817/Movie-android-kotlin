package us.bojie.itp.ui.main.state

sealed class MainStateEvent {

    class GetMoviesEvent: MainStateEvent()

    class None: MainStateEvent()

}