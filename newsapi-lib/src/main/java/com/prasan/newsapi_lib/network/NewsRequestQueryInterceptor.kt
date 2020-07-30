package com.prasan.newsapi_lib.network

import com.prasan.newsapi_lib.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

internal class NewsRequestQueryInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request()
            .newBuilder()
            .addHeader("X-Api-Key", BuildConfig.API_KEY)
        return chain.proceed(requestBuilder.build())
    }
}