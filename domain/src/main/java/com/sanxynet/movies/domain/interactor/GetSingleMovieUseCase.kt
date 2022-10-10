package com.sanxynet.movies.domain.interactor

import com.sanxynet.movies.domain.repository.MoviesRemoteRepository
import com.sanxynet.movies.model.MovieDetail
import io.reactivex.Single

class GetSingleMovieUseCase(private val moviesRemoteRepository: MoviesRemoteRepository) {

    fun execute(id: String): Single<MovieDetail> {
        return moviesRemoteRepository.getSingleMovie(id)
    }

}