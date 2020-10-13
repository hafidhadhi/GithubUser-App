package com.hafidhadhi.consumerapp.data

import androidx.paging.PagingData
import com.hafidhadhi.consumerapp.data.local.FavoriteUser
import kotlinx.coroutines.flow.Flow

interface GithubUserDataSource {
    fun getFavUser(): Flow<PagingData<FavoriteUser>>
}