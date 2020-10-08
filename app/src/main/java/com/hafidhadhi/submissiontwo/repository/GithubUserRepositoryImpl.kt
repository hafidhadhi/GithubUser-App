package com.hafidhadhi.submissiontwo.repository

import androidx.paging.PagingData
import com.hafidhadhi.submissiontwo.data.remote.GithubUserRemoteDataSource
import com.hafidhadhi.submissiontwo.data.remote.dto.GithubUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GithubUserRepositoryImpl @Inject constructor(private val githubUserRemoteDataSource: GithubUserRemoteDataSource) :
    GithubUserRepository {
    override fun searchUser(name: String): Flow<PagingData<GithubUser>> {
        return githubUserRemoteDataSource.searchUser(name)
    }

    override suspend fun getUser(name: String): GithubUser {
        return withContext(Dispatchers.IO) {
            githubUserRemoteDataSource.getUser(name)
        }
    }
}