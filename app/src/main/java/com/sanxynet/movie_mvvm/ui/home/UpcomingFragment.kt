package com.sanxynet.movie_mvvm.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.core.util.Pair as UtilPair
import com.google.android.material.snackbar.Snackbar
import com.sanxynet.movie_mvvm.common.recyclerview.PaginationScrollListener
import com.sanxynet.movie_mvvm.common.utils.gone
import com.sanxynet.movie_mvvm.common.utils.setAnchorId
import com.sanxynet.movie_mvvm.common.utils.visible
import com.sanxynet.movie_mvvm.data.Resource
import com.sanxynet.movie_mvvm.ui.home.master.MovieListAdapter
import com.sanxynet.movie_mvvm.ui.home.viewmodel.UpcomingViewModel
import com.sanxynet.movie_mvvm.R
import com.sanxynet.movie_mvvm.databinding.FragmentMovieListBinding
import com.sanxynet.movies.model.Movie
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class UpcomingFragment : Fragment(R.layout.fragment_movie_list), MovieListAdapter.OnItemClickListener {

    private val upcomingViewModel: UpcomingViewModel by sharedViewModel()
    private val movieListAdapter: MovieListAdapter by inject()

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecyclerView()
        setupSwipeRefresh()

        upcomingViewModel.refreshUpcomingMovies()

        viewLifecycleOwner.lifecycleScope.launch {
            upcomingViewModel.upcomingMoviesState.collect {
                handleMoviesDataState(it)
            }
        }
    }

    override fun onItemClick(movie: Movie, container: View) {
        val action = UpcomingFragmentDirections.navigateToMovieDetails(id = movie.id, posterPath = movie.poster_path)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(),
            UtilPair.create(container, container.transitionName)
        )

        findNavController().navigate(action, ActivityNavigatorExtras(options))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        upcomingViewModel.disposable?.dispose()
    }

    private fun handleMoviesDataState(state: Resource<List<Movie>>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                binding.srlFragmentMovieList.isRefreshing = true
            }
            Resource.Status.SUCCESS -> {
                binding.srlFragmentMovieList.isRefreshing = false
                loadMovies(state.data)
            }
            Resource.Status.ERROR -> {
                binding.srlFragmentMovieList.isRefreshing = false
                binding.pbFragmentMovieList.gone()
                Snackbar.make(binding.srlFragmentMovieList, getString(R.string.error_message_pattern, state.message), Snackbar.LENGTH_LONG)
                    .setAnchorId(R.id.bottom_navigation).show()
            }
            Resource.Status.EMPTY -> {
                Timber.d("Empty state.")
            }
        }
    }

    private fun loadMovies(movies: List<Movie>?) {
        movies?.let {
            if (upcomingViewModel.isFirstPage()) {
                // Remove previous movies
                movieListAdapter.clear()
            }

            movieListAdapter.fillList(it)
        }
    }

    private fun setupRecyclerView() {
        movieListAdapter.setOnMovieClickListener(this)

        binding.rvFragmentMovieList.adapter = movieListAdapter
        binding.rvFragmentMovieList.addOnScrollListener(object : PaginationScrollListener(binding.rvFragmentMovieList.linearLayoutManager) {
            override fun isLoading(): Boolean {
                val isLoading = binding.srlFragmentMovieList.isRefreshing

                if (isLoading) {
                    binding.pbFragmentMovieList.visible()
                } else {
                    binding.pbFragmentMovieList.gone()
                }

                return isLoading
            }

            override fun isLastPage(): Boolean {
                return upcomingViewModel.isLastPage()
            }

            override fun loadMoreItems() {
                upcomingViewModel.fetchNextUpcomingMovies()
            }
        })
    }

    private fun setupSwipeRefresh() {
        binding.srlFragmentMovieList.setOnRefreshListener {
            upcomingViewModel.refreshUpcomingMovies()
        }
    }
}