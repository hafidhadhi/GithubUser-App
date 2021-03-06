package com.hafidhadhi.submissiontwo.data.remote

import com.hafidhadhi.submissiontwo.data.remote.dto.GithubUser
import com.hafidhadhi.submissiontwo.data.remote.dto.SearchUser
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {

    @GET("search/users")
    fun searchUserAsync(
        @Query("q") name: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10
    ): Deferred<retrofit2.Response<SearchUser>>

    @GET("users/{username}")
    fun getUserAsync(@Path("username") name: String): Deferred<GithubUser>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") name: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10
    ): Call<ResponseBody>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") name: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10
    ): Call<ResponseBody>
}