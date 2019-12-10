package us.bojie.itp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_detail.*
import us.bojie.itp.R
import us.bojie.itp.model.Movie


class DetailFragment : Fragment() {

    private val TAG: String = "AppDebug"

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        savedInstanceState?.let { inState ->
            (inState["movie"] as Movie?)?.let { movie ->
                viewModel.setMovie(movie)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("movie", viewModel.viewState.value?.movie)
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            if (viewState != null) {
                viewState.movie?.let {
                    setDetails(it)
                }
            }
        })
    }

    private fun setDetails(movie: Movie) {
        val requestOptions = RequestOptions
            .overrideOf(1920, 1080)
        Glide.with(this@DetailFragment)
            .applyDefaultRequestOptions(requestOptions)
            .load(movie.Poster)
            .into(image)
        title.text = movie.Title
        body.text = movie.imdbID
    }
}
