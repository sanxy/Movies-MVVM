package com.sanxynet.movies.domain.repository

import com.sanxynet.movies.model.MovieDetail
import com.sanxynet.movies.model.MoviesResponse
import io.reactivex.Single

interface MoviesRemoteRepository {

    fun getPopularMovies(page: Int): Single<MoviesResponse>

    fun getUpcomingMovies(page: Int): Single<MoviesResponse>

    fun getSingleMovie(id: String): Single<MovieDetail>

}