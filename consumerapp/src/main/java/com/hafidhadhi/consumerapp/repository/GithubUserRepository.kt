package com.hafidhadhi.consumerapp.repository

import androidx.paging.PagingData
import com.hafidhadhi.consumerapp.data.local.FavoriteUser
import kotlinx.coroutines.flow.Flow

interface GithubUserRepository {
    fun getFavUser(): Flow<PagingData<FavoriteUser>>
}