package com.sanxynet.movies.data.sources.remote.auth

import com.sanxynet.movies.data.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class MoviesAuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url: HttpUrl = chain.request().url.newBuilder()
            .addQueryParameter("api_key", BuildConfig.THE_MOVIE_DB_API_KEY)
            .build()

        val request = chain.request().newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)
    }
}