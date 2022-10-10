package com.sanxynet.movie_mvvm.di

import com.sanxynet.movie_mvvm.ui.details.viewmodel.MovieDetailsViewModel
import com.sanxynet.movie_mvvm.ui.home.master.MovieListAdapter
import com.sanxynet.movie_mvvm.ui.home.viewmodel.FavoriteViewModel
import com.sanxynet.movie_mvvm.ui.home.viewmodel.PopularViewModel
import com.sanxynet.movie_mvvm.ui.home.viewmodel.UpcomingViewModel
import com.sanxynet.movies.data.repository.MoviesLocalRepositoryImpl
import com.sanxynet.movies.data.repository.MoviesRemoteRepositoryImpl
import com.sanxynet.movies.data.sources.local.mapper.MoviesLocalMapper
import com.sanxynet.movies.data.sources.remote.mapper.MoviesRemoteMapper
import com.sanxynet.movies.domain.interactor.*
import com.sanxynet.movies.domain.repository.MoviesLocalRepository
import com.sanxynet.movies.domain.repository.MoviesRemoteRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    single { MoviesRemoteMapper() }
    single { MoviesLocalMapper() }
    factory<MoviesRemoteRepository> { MoviesRemoteRepositoryImpl(get()) }
    factory<MoviesLocalRepository> { MoviesLocalRepositoryImpl(androidContext(), get()) }
    factory { MovieListAdapter(androidContext()) }
}

val popularMoviesModule = module {
    factory { GetPopularMoviesUseCase(get()) }
    viewModel { PopularViewModel(get()) }
}

val upcomingMoviesModule = module {
    factory { GetUpcomingMoviesUseCase(get()) }
    viewModel { UpcomingViewModel(get()) }
}

val favoriteMoviesModule = module {
    factory { GetFavoriteMoviesUseCase(get()) }
    viewModel { FavoriteViewModel(get()) }
}

val movieDetailsModule = module {
    factory { GetSingleMovieUseCase(get()) }
    factory { AddFavoriteMovieUseCase(get()) }
    factory { DeleteFavoriteMovieUseCase(get()) }
    factory { UpdateFavoriteMovieUseCase(get()) }
    factory { GetFavoriteMovieUseCase(get()) }
    viewModel { MovieDetailsViewModel(get(), get(), get(), get(), get()) }
}