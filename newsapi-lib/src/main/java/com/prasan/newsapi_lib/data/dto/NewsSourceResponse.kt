package com.prasan.newsapi_lib.data.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NewsSourceResponse(
    @Json(name = "sources")
    val sources: List<Source>,
    @Json(name = "status")
    val status: String,
    @Json(name = "code")
    val code: String?,
    @Json(name = "message")
    val error: String?
)