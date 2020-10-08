package com.hafidhadhi.submissiontwo.data.remote

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.hafidhadhi.submissiontwo.data.GithubUserDataSource
import com.hafidhadhi.submissiontwo.data.remote.dto.GithubUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GithubUserRemoteDataSource @Inject constructor(private val service: GithubService) :
    GithubUserDataSource {
    override fun searchUser(name: String): Flow<PagingData<GithubUser>> {
        return Pager(
            config = PagingConfig(10),
            pagingSourceFactory = { SearchUserPagingSource(service, name) }
        ).flow
    }

    override suspend fun getUser(name: String): GithubUser = service.getUserAsync(name).await()
}

class SearchUserPagingSource(private val service: GithubService, private val name: String) :
    PagingSource<Int, GithubUser>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubUser> {
        return try {
            val position = params.key ?: 1
            val user = service.searchUserAsync(name, position).await().users
                ?: throw  Exception("No Data")
            LoadResult.Page(
                data = user.map { it },
                prevKey = if (position == 1) null else position - 1,
                nextKey = position + 1
            )
        } catch (e: Exception) {
            Log.e(this::class.java.simpleName, e.message.toString(), e)
            LoadResult.Error(e)
        }
    }

}