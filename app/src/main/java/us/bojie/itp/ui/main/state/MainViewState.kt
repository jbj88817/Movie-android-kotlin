package us.bojie.itp.ui.main.state

import us.bojie.itp.model.Movie
import us.bojie.itp.model.MovieResponse

data class MainViewState(

    var movieResponse: MovieResponse? = null,

    var movie: Movie? = null

)