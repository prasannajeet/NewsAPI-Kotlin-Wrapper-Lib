package com.prasan.newsapi_lib.data

import com.prasan.newsapi_lib.BuildConfig
import com.prasan.newsapi_lib.data.network.NewsAPI
import com.prasan.newsapi_lib.data.network.NewsRequestQueryInterceptor
import com.prasan.newsapi_lib.performSafeNetworkApiCall
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal object NewsApiRepository {

    private fun getRetrofit(apiKey: String) =
        Retrofit.Builder().run {
            baseUrl(BuildConfig.BASE_URL)
            addConverterFactory(MoshiConverterFactory.create())
            client(OkHttpClient.Builder().run {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                addInterceptor(
                    NewsRequestQueryInterceptor(
                        apiKey
                    )
                )
                build()
            })
            build()
        }.run {
            create(NewsAPI::class.java)
        }

    @ExperimentalCoroutinesApi
    suspend fun getNewsSources(apiKey: String) =
        performSafeNetworkApiCall {
            getRetrofit(apiKey).getNewsSources()
        }
}