package com.sanxynet.movie_mvvm.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sanxynet.movie_mvvm.common.utils.setAnchorId
import com.sanxynet.movie_mvvm.data.Resource
import com.sanxynet.movie_mvvm.R
import com.sanxynet.movie_mvvm.databinding.FragmentMovieListBinding
import com.sanxynet.movie_mvvm.ui.home.master.MovieListAdapter
import com.sanxynet.movie_mvvm.ui.home.viewmodel.FavoriteViewModel
import com.sanxynet.movies.model.Movie
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class FavoriteFragment : Fragment(R.layout.fragment_movie_list), MovieListAdapter.OnItemClickListener {

    private val favoriteViewModel: FavoriteViewModel by sharedViewModel()
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

        favoriteViewModel.fetchFavoriteMovies()

        viewLifecycleOwner.lifecycleScope.launch {
            favoriteViewModel.favoriteMoviesState.collect {
                handleFavoriteMoviesDataState(it)
            }
        }
    }

    override fun onItemClick(movie: Movie, container: View) {
        val action = FavoriteFragmentDirections.navigateToMovieDetails(id = movie.id, posterPath = movie.poster_path)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(),
            Pair.create(container, container.transitionName)
        )

        findNavController().navigate(action, ActivityNavigatorExtras(options))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        favoriteViewModel.disposable?.dispose()
    }

    private fun handleFavoriteMoviesDataState(state: Resource<List<Movie>>) {
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
            movieListAdapter.clear()
            movieListAdapter.fillList(it)
        }
    }

    private fun setupRecyclerView() {
        movieListAdapter.setOnMovieClickListener(this)

        binding.rvFragmentMovieList.adapter = movieListAdapter
    }

    private fun setupSwipeRefresh() {
        binding.srlFragmentMovieList.setOnRefreshListener {
            favoriteViewModel.fetchFavoriteMovies()
        }
    }
}