package com.prasan.newsapi_lib.data.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EverythingResponse(
    @Json(name = "articles")
    val articles: List<Article>,
    @Json(name = "status")
    val status: String,
    @Json(name = "totalResults")
    val totalResults: Int,
    @Json(name = "code")
    val code: String?,
    @Json(name = "message")
    val error: String?
)