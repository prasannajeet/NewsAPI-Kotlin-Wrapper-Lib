package com.prasan.newsapi_lib

import com.prasan.newsapi_lib.data.dto.NewsSourceResponse
import com.prasan.newsapi_lib.domain.GetNewsSourcesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

object NewsAPIHandler {

    private var apiKey: String? = null

    fun with(apiKey: String): NewsAPIHandler {
        this.apiKey = apiKey
        return this
    }

    @ExperimentalCoroutinesApi
    suspend fun getNewsSources(
        onSuccess: (NewsSourceResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {

        if (this.apiKey == null) {
            withContext(Dispatchers.Main) {
                onError(Throwable("Please provide an API Key using the NewsAPIHandler#with() method"))
            }
            return
        }

        this.apiKey?.let { str ->
            GetNewsSourcesUseCase(str).execute(Unit).collect {
                when (it) {
                    is ViewState.RenderSuccess ->
                        withContext(Dispatchers.Main) {
                            onSuccess(it.output)
                        }
                    is ViewState.RenderFailure ->
                        withContext(Dispatchers.Main) {
                            onError(it.throwable)
                        }
                }
            }
        }
    }
}