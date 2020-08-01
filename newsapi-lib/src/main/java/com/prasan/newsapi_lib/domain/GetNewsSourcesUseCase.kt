package com.prasan.newsapi_lib.domain

import com.prasan.newsapi_lib.State
import com.prasan.newsapi_lib.data.NewsApiRepository
import com.prasan.newsapi_lib.data.dto.NewsSourceResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class GetNewsSourcesUseCase(override var apiKey: String) :
    UseCase<Unit, NewsSourceResponse> {

    @ExperimentalCoroutinesApi
    override suspend fun execute(input: Unit) = flow {
        NewsApiRepository.getNewsSources(apiKey).map {
            when (it) {
                is State.Success -> {
                    it.output.error?.let { errorMsg ->
                        State.Failure(Throwable(errorMsg))
                    } ?: State.Success(it.output)
                }
                else -> it
            }
        }.collect {
            emit(it)
        }
    }
}