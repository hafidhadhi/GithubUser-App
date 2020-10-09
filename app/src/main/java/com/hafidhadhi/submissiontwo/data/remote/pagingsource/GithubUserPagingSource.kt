package com.hafidhadhi.submissiontwo.data.remote.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import com.hafidhadhi.submissiontwo.data.remote.GithubPageLinks
import com.hafidhadhi.submissiontwo.data.remote.GithubService
import com.hafidhadhi.submissiontwo.data.remote.dto.GithubUser
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

class SearchUserPagingSource(private val service: GithubService, private val name: String) :
    PagingSource<Int, GithubUser>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubUser> {
        return try {
            val position = params.key ?: 1
            val response = service.searchUserAsync(name, position).await()
            val pageLinks = GithubPageLinks(response.raw())
            val users = response.body()?.users ?: throw  HttpException(response)
            LoadResult.Page(
                data = users.map { it },
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (pageLinks.next.isNullOrEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            Log.e(this::class.java.simpleName, e.message.toString(), e)
            LoadResult.Error(e)
        }
    }
}

class FollowerPagingSource(
    private val service: GithubService,
    private val moshi: Moshi,
    private val name: String
) :
    PagingSource<Int, GithubUser>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubUser> {
        return try {
            val position = params.key ?: 1
            val response = withContext(Dispatchers.IO) { callService(name, position) }
            val responseStr =
                withContext(Dispatchers.Default) {
                    stringifyResponse(
                        response.body() ?: throw HttpException(response)
                    )
                }
            val pageLinks = GithubPageLinks(response.raw())
            val type = Types.newParameterizedType(List::class.java, GithubUser::class.java)
            val adapter = moshi.adapter<List<GithubUser>>(type)
            val users = withContext(Dispatchers.Default) { fromJson(responseStr, adapter) }
            LoadResult.Page(
                data = users.map { it },
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (pageLinks.next.isNullOrEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            Log.e(this::class.java.simpleName, e.message.toString(), e)
            LoadResult.Error(e)
        }
    }

    @Throws(Exception::class)
    private fun callService(name: String, page: Int): Response<ResponseBody> {
        return service.getFollowers(name, page).execute()
    }

    @Throws(Exception::class)
    private fun stringifyResponse(response: ResponseBody): String {
        return response.string()
    }

    @Throws(Exception::class)
    private fun fromJson(string: String, adapter: JsonAdapter<List<GithubUser>>): List<GithubUser> {
        return adapter.fromJson(string) ?: throw  Exception("Error Parsing JSON")
    }
}

class FollowingPagingSource(
    private val service: GithubService,
    private val moshi: Moshi,
    private val name: String
) :
    PagingSource<Int, GithubUser>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubUser> {
        return try {
            val position = params.key ?: 1
            val response = withContext(Dispatchers.IO) { callService(name, position) }
            val responseStr =
                withContext(Dispatchers.Default) {
                    stringifyResponse(
                        response.body() ?: throw HttpException(response)
                    )
                }
            val pageLinks = GithubPageLinks(response.raw())
            val type = Types.newParameterizedType(List::class.java, GithubUser::class.java)
            val adapter = moshi.adapter<List<GithubUser>>(type)
            val users = withContext(Dispatchers.Default) { fromJson(responseStr, adapter) }
            LoadResult.Page(
                data = users.map { it },
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (pageLinks.next.isNullOrEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            Log.e(this::class.java.simpleName, e.message.toString(), e)
            LoadResult.Error(e)
        }
    }

    @Throws(Exception::class)
    private fun callService(name: String, page: Int): Response<ResponseBody> {
        return service.getFollowing(name, page).execute()
    }

    @Throws(Exception::class)
    private fun stringifyResponse(response: ResponseBody): String {
        return response.string()
    }

    @Throws(Exception::class)
    private fun fromJson(string: String, adapter: JsonAdapter<List<GithubUser>>): List<GithubUser> {
        return adapter.fromJson(string) ?: throw  Exception("Error Parsing JSON")
    }
}