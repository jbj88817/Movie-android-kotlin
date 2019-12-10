package us.bojie.itp.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.main_fragment.*
import us.bojie.itp.R
import us.bojie.itp.model.Movie
import us.bojie.itp.ui.DataStateListener
import us.bojie.itp.ui.main.state.MainStateEvent.GetMoviesEvent

class MainFragment : Fragment(), MainRecyclerAdapter.Interaction {

    private lateinit var viewModel: MainViewModel

    private lateinit var dataStateHandler: DataStateListener

    private lateinit var mainRecyclerAdapter: MainRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        initRecyclerView()
        subscribeObservers()
        triggerGetMoviesEvent()
    }

    private fun initRecyclerView() {
        main_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainFragment.context)
//            val topSpacingDecorator = TopSpacingItemDecoration(30)
//            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
//            addItemDecoration(topSpacingDecorator)

            val requestOptions = RequestOptions
                .placeholderOf(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            mainRecyclerAdapter = MainRecyclerAdapter(
                this@MainFragment,
                Glide.with(this@MainFragment)
                    .applyDefaultRequestOptions(requestOptions)
            )

            adapter = mainRecyclerAdapter
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->

            if (dataState != null) {
                // Handle Loading and Message
                dataStateHandler.onDataStateChange(dataState)

                // handle Data<T>
                dataState.data?.let { event ->
                    event.getContentIfNotHandled()?.let { mainViewState ->

                        println("DEBUG: DataState: ${mainViewState}")

                        viewModel.setMovieResponseData(mainViewState.movieResponse)
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->

            if (viewState != null) {
                // set BlogPosts to RecyclerView
                viewState.movieResponse?.Movies?.let {
                    mainRecyclerAdapter.submitList(
                        list = it
                    )
                }
            }
        })
    }

    private fun triggerGetMoviesEvent() {
        viewModel.setStateEvent(GetMoviesEvent())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dataStateHandler = context as DataStateListener
        } catch (e: ClassCastException) {
            println("$context must implement DataStateListener")
        }
    }

    override fun onItemSelected(position: Int, item: Movie) {
        viewModel.setMovie(item)
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(
                R.id.fragment_container,
                DetailFragment::class.java,
                null
            )
            ?.addToBackStack("DetailFragment")
            ?.commit()
    }

    override fun onDeleteButtonClicked(position: Int, item: Movie) {
        viewModel.getCurrentViewStateOrNew().movieResponse?.let {
            val movieList = it.Movies.toMutableList()
            val deletedList = movieList - item
            it.Movies = deletedList
            viewModel.setMovieResponseData(it)
            mainRecyclerAdapter.submitList(deletedList)
        }
    }
}
