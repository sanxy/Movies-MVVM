package com.sanxynet.movies.domain.interactor

import com.sanxynet.movies.domain.repository.MoviesLocalRepository
import com.sanxynet.movies.model.Movie
import io.reactivex.Observable
import io.reactivex.Single

class GetFavoriteMoviesUseCase(private val moviesLocalRepository: MoviesLocalRepository) {

    fun execute(): Observable<List<Movie>> {
        return moviesLocalRepository.getFavoriteMovies()
    }

}