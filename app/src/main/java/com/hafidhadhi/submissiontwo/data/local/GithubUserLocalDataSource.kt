package com.hafidhadhi.submissiontwo.data.local

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hafidhadhi.submissiontwo.data.GithubUserDataSource
import com.hafidhadhi.submissiontwo.data.local.entity.FavoriteUserEnt
import com.hafidhadhi.submissiontwo.data.local.entity.toContentValues
import com.hafidhadhi.submissiontwo.data.local.pagingsource.FavoritePagingSource
import com.hafidhadhi.submissiontwo.data.remote.dto.GithubUser
import com.hafidhadhi.submissiontwo.provider.uriBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject
import javax.inject.Named

class GithubUserLocalDataSource @Inject constructor(
    private val favoriteUserDao: FavoriteUserDao,
    @ApplicationContext private val context: Context,
    @Named("Dispatchers.IO") private val iODispatcher: CoroutineDispatcher
) :
    GithubUserDataSource {
    override fun searchUser(name: String): Flow<PagingData<GithubUser>> {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(name: String): GithubUser {
        TODO("Not yet implemented")
    }

    override fun getFollowers(name: String): Flow<PagingData<GithubUser>> {
        TODO("Not yet implemented")
    }

    override fun getFollowing(name: String): Flow<PagingData<GithubUser>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertOrDeleteFavUser(favoriteUserEnt: FavoriteUserEnt) {
        val contentResolver = context.contentResolver
        val uri = uriBuilder.build()
        contentResolver.insert(uri, favoriteUserEnt.toContentValues())
    }

    override fun getFavUser(): Flow<PagingData<FavoriteUserEnt>> {
        return Pager(
            config = PagingConfig(10),
            pagingSourceFactory = { FavoritePagingSource(context, iODispatcher) }
        ).flow
    }

    override fun isFavorite(id: Int): Flow<Boolean> {
        return favoriteUserDao.isFavorite(id).mapNotNull { it.isNotEmpty() }
    }
}