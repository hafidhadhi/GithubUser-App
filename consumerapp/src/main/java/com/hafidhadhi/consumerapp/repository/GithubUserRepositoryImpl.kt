package com.hafidhadhi.consumerapp.repository

import androidx.paging.PagingData
import com.hafidhadhi.consumerapp.data.GithubUserDataSource
import com.hafidhadhi.consumerapp.data.local.FavoriteUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GithubUserRepositoryImpl @Inject constructor(
    private val githubUserLocalDataSource: GithubUserDataSource
) :
    GithubUserRepository {
    override fun getFavUser(): Flow<PagingData<FavoriteUser>> {
        return githubUserLocalDataSource.getFavUser()
    }
}