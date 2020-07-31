package com.prasan.newsapi_lib.data.network

import com.prasan.newsapi_lib.data.dto.NewsSourceResponse
import retrofit2.Response
import retrofit2.http.GET

internal interface NewsAPI {
    @GET("v2/sources")
    suspend fun getNewsSources(): Response<NewsSourceResponse>
}