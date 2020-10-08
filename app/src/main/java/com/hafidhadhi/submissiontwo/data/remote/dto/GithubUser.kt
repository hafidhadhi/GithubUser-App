package com.hafidhadhi.submissiontwo.data.remote.dto

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class GithubUser(
    val id: Int? = null,
    @Json(name = "login") val userName: String? = null,
    val name: String? = null,
    @Json(name = "avatar_url") val avatarUrl: String? = null,
    @Json(name = "html_url") val profileUrl: String? = null,
    val company: String? = null,
    val location: String? = null,
    @Json(name = "public_repos") val repos: Int? = null,
    val followers: Int? = null,
    val following: Int? = null
) : Parcelable