package com.hafidhadhi.submissiontwo.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hafidhadhi.submissiontwo.data.GithubUserDataSource
import com.hafidhadhi.submissiontwo.data.remote.dto.GithubUser
import com.hafidhadhi.submissiontwo.data.remote.pagingsource.FollowerPagingSource
import com.hafidhadhi.submissiontwo.data.remote.pagingsource.FollowingPagingSource
import com.hafidhadhi.submissiontwo.data.remote.pagingsource.SearchUserPagingSource
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GithubUserRemoteDataSource @Inject constructor(
    private val service: GithubService,
    private val moshi: Moshi
) :
    GithubUserDataSource {
    override fun searchUser(name: String): Flow<PagingData<GithubUser>> {
        return Pager(
            config = PagingConfig(10),
            pagingSourceFactory = { SearchUserPagingSource(service, name) }
        ).flow
    }

    override suspend fun getUser(name: String): GithubUser = service.getUserAsync(name).await()
    override fun getFollowers(name: String): Flow<PagingData<GithubUser>> {
        return Pager(
            config = PagingConfig(10),
            pagingSourceFactory = { FollowerPagingSource(service, moshi, name) }
        ).flow
    }

    override fun getFollowing(name: String): Flow<PagingData<GithubUser>> {
        return Pager(
            config = PagingConfig(10),
            pagingSourceFactory = { FollowingPagingSource(service, moshi, name) }
        ).flow
    }
}