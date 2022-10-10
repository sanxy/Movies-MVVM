package com.sanxynet.movies.domain.interactor

import com.sanxynet.movies.domain.repository.MoviesRemoteRepository
import com.sanxynet.movies.model.MoviesResponse
import io.reactivex.Single

class GetUpcomingMoviesUseCase(private val moviesRemoteRepository: MoviesRemoteRepository) {

    fun execute(page: Int): Single<MoviesResponse> {
        return moviesRemoteRepository.getUpcomingMovies(page)
    }

}