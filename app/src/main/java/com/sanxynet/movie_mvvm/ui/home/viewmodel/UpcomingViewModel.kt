package com.sanxynet.movie_mvvm.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import com.sanxynet.movie_mvvm.data.Resource
import com.sanxynet.movies.domain.interactor.GetUpcomingMoviesUseCase
import com.sanxynet.movies.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UpcomingViewModel(private val getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase) : ViewModel() {

    private val stateFlow = MutableStateFlow<Resource<List<Movie>>>(Resource.empty())
    private var currentPage = 1
    private var lastPage = 1

    var disposable: Disposable? = null

    val upcomingMoviesState: StateFlow<Resource<List<Movie>>>
        get() = stateFlow

    fun fetchUpcomingMovies() {
        stateFlow.value = Resource.loading()

        disposable = getUpcomingMoviesUseCase.execute(currentPage)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ res ->
                lastPage = res.total_pages
                stateFlow.value = Resource.success(res.results)
            }, { throwable ->
                lastPage = currentPage // prevent loading more pages
                throwable.localizedMessage?.let {
                    stateFlow.value = Resource.error(it)
                }
            })
    }

    fun fetchNextUpcomingMovies() {
        currentPage++
        fetchUpcomingMovies()
    }

    fun refreshUpcomingMovies() {
        currentPage = 1
        fetchUpcomingMovies()
    }

    fun isFirstPage(): Boolean {
        return currentPage == 1
    }

    fun isLastPage(): Boolean {
        return currentPage == lastPage
    }

}