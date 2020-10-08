package com.hafidhadhi.submissiontwo.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchUser(
    @Json(name = "total_count") val totalCount: Long?,
    @Json(name = "items") val users: List<GithubUser>?
)