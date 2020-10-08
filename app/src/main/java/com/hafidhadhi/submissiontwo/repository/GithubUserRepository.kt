package com.hafidhadhi.submissiontwo.repository

import androidx.paging.PagingData
import com.hafidhadhi.submissiontwo.data.remote.dto.GithubUser
import kotlinx.coroutines.flow.Flow

interface GithubUserRepository {
    fun searchUser(name: String): Flow<PagingData<GithubUser>>
    suspend fun getUser(name: String): GithubUser
    fun getFollowers(name: String): Flow<PagingData<GithubUser>>
    fun getFollowing(name: String): Flow<PagingData<GithubUser>>

}