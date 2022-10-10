package com.sanxynet.movies.data.repository

import com.sanxynet.movies.data.sources.remote.api.ApiClient
import com.sanxynet.movies.data.sources.remote.mapper.MoviesRemoteMapper
import com.sanxynet.movies.domain.repository.MoviesRemoteRepository
import com.sanxynet.movies.model.MovieDetail
import com.sanxynet.movies.model.MoviesResponse
import io.reactivex.Single

class MoviesRemoteRepositoryImpl(private val moviesRemoteMapper: MoviesRemoteMapper) : MoviesRemoteRepository  {

    override fun getPopularMovies(page: Int): Single<MoviesResponse> {
        return ApiClient.movieService().getPopularMovies(page).map {
            moviesRemoteMapper.mapFromRemote(it)
        }
    }

    override fun getUpcomingMovies(page: Int): Single<MoviesResponse> {
        return ApiClient.movieService().getUpcomingMovies(page).map {
            moviesRemoteMapper.mapFromRemote(it)
        }
    }

    override fun getSingleMovie(id: String): Single<MovieDetail> {
        return ApiClient.movieService().getSingleMovie(id).map {
            moviesRemoteMapper.mapDetailFromRemote(it)
        }
    }

}