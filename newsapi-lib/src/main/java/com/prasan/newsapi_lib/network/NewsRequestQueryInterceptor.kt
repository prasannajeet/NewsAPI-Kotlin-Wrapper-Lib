package com.prasan.newsapi_lib.network

import okhttp3.Interceptor
import okhttp3.Response

internal class NewsRequestQueryInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request()
            .newBuilder()
            .addHeader("X-Api-Key", apiKey)
        return chain.proceed(requestBuilder.build())
    }
}