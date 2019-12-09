package us.bojie.itp.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.main_fragment.*
import us.bojie.itp.R
import us.bojie.itp.model.Movie
import us.bojie.itp.ui.DataStateListener
import us.bojie.itp.ui.main.state.MainStateEvent.GetMoviesEvent
import us.bojie.itp.util.TopSpacingItemDecoration

class MainFragment : Fragment(), MainRecyclerAdapter.Interaction {

    companion object {
        fun newInstance() = MainFragment()
    }

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
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        initRecyclerView()
        subscribeObservers()
        triggerGetMoviesEvent()
    }

    private fun initRecyclerView() {
        main_recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainFragment.context)
            val topSpacingDecorator = TopSpacingItemDecoration(30)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            val requestOptions = RequestOptions
                .placeholderOf(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)

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
        Log.d("MainFragment", "onItemSelected (line 117): $position $item")
    }

    override fun onDeleteButtonClicked(position: Int, item: Movie) {
        Log.d("MainFragment", "onDeleteButtonClicked (line 121): ")
    }

}