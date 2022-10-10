package com.sanxynet.movies.domain.interactor

import com.sanxynet.movies.domain.repository.MoviesLocalRepository
import com.sanxynet.movies.model.Movie
import io.reactivex.Single

class GetFavoriteMovieUseCase(private val moviesLocalRepository: MoviesLocalRepository) {

    fun execute(id: Int): Single<Movie> {
        return moviesLocalRepository.getFavoriteMovie(id)
    }

}