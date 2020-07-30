package com.prasan.newsapi_lib.domain

import com.prasan.newsapi_lib.NetworkOperationResult
import com.prasan.newsapi_lib.ViewState
import com.prasan.newsapi_lib.data.NewsApiRepository
import com.prasan.newsapi_lib.data.dto.NewsSourceResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class GetNewsSourcesUseCase() :
    UseCase<Unit, NewsSourceResponse> {

    @ExperimentalCoroutinesApi
    override suspend fun execute(input: Unit): Flow<ViewState<NewsSourceResponse>> = flow {
        NewsApiRepository.getNewsSources().collect {
            when (it) {
                is NetworkOperationResult.OnSuccess -> {
                    it.data.error?.let { errorMsg ->
                        emit(ViewState.RenderFailure(Throwable(errorMsg)))
                    } ?: emit(ViewState.RenderSuccess(it.data))
                }
                is NetworkOperationResult.OnFailed -> emit(ViewState.RenderFailure(it.throwable))
            }
        }
    }
}