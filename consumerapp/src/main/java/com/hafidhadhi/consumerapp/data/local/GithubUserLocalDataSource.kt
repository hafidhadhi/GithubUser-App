package com.hafidhadhi.consumerapp.data.local

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hafidhadhi.consumerapp.data.GithubUserDataSource
import com.hafidhadhi.submissiontwo.data.local.pagingsource.FavoritePagingSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GithubUserLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val iODispatcher: CoroutineDispatcher
) :
    GithubUserDataSource {
    override fun getFavUser(): Flow<PagingData<FavoriteUser>> {
        return Pager(
            config = PagingConfig(10),
            pagingSourceFactory = { FavoritePagingSource(context, iODispatcher) }
        ).flow
    }
}