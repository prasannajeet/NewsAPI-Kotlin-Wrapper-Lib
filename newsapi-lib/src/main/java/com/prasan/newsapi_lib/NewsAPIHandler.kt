package com.prasan.newsapi_lib

import com.prasan.newsapi_lib.data.dto.NewsSourceResponse
import com.prasan.newsapi_lib.domain.GetNewsSourcesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

object NewsAPIHandler {
    @ExperimentalCoroutinesApi
    suspend fun getNewsSources(
        onSuccess: (NewsSourceResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) = GetNewsSourcesUseCase().execute(Unit).collect {
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