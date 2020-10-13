package com.hafidhadhi.submissiontwo.data

import androidx.paging.PagingData
import com.hafidhadhi.submissiontwo.data.local.entity.FavoriteUserEnt
import com.hafidhadhi.submissiontwo.data.remote.dto.GithubUser
import kotlinx.coroutines.flow.Flow

interface GithubUserDataSource {
    fun searchUser(name: String): Flow<PagingData<GithubUser>>
    suspend fun getUser(name: String): GithubUser
    fun getFollowers(name: String): Flow<PagingData<GithubUser>>
    fun getFollowing(name: String): Flow<PagingData<GithubUser>>
    suspend fun insertOrDeleteFavUser(favoriteUserEnt: FavoriteUserEnt)
    fun getFavUser(): Flow<PagingData<FavoriteUserEnt>>
    fun isFavorite(id: Int): Flow<Boolean>
}