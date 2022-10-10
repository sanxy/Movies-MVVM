package com.sanxynet.movies.domain.interactor

import com.sanxynet.movies.domain.repository.MoviesLocalRepository
import com.sanxynet.movies.model.Movie
import io.reactivex.Completable
import io.reactivex.Single

class DeleteFavoriteMovieUseCase(private val moviesLocalRepository: MoviesLocalRepository) {

    fun execute(movie: Movie): Completable {
        return moviesLocalRepository.deleteFavoriteMovie(movie)
    }

}