package com.hafidhadhi.submissiontwo.repository

import androidx.paging.PagingData
import com.hafidhadhi.submissiontwo.data.GithubUserDataSource
import com.hafidhadhi.submissiontwo.data.local.entity.FavoriteUserEnt
import com.hafidhadhi.submissiontwo.data.remote.dto.GithubUser
import com.hafidhadhi.submissiontwo.di.AppModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class GithubUserRepositoryImpl @Inject constructor(
    @AppModule.GithubUserRemoteDataSource private val githubUserRemoteDataSource: GithubUserDataSource,
    @AppModule.GithubUserLocalDataSource private val githubUserLocalDataSource: GithubUserDataSource,
    @Named("Dispatchers.IO") private val iODispatcher: CoroutineDispatcher
) :
    GithubUserRepository {
    override fun searchUser(name: String): Flow<PagingData<GithubUser>> {
        return githubUserRemoteDataSource.searchUser(name)
    }

    override suspend fun getUser(name: String): GithubUser {
        return withContext(iODispatcher) {
            githubUserRemoteDataSource.getUser(name)
        }
    }

    override fun getFollowers(name: String): Flow<PagingData<GithubUser>> {
        return githubUserRemoteDataSource.getFollowers(name)
    }

    override fun getFollowing(name: String): Flow<PagingData<GithubUser>> {
        return githubUserRemoteDataSource.getFollowing(name)
    }

    override suspend fun insertOrDeleteFavUser(githubUser: GithubUser) {
        withContext(iODispatcher) {
            githubUserLocalDataSource.insertOrDeleteFavUser(
                FavoriteUserEnt(
                    id = githubUser.id ?: 0,
                    userName = githubUser.userName,
                    name = githubUser.name,
                    avatarUrl = githubUser.avatarUrl,
                    profileUrl = githubUser.profileUrl,
                    company = githubUser.company,
                    location = githubUser.location,
                    repos = githubUser.repos,
                    followers = githubUser.followers,
                    following = githubUser.following
                )
            )
        }
    }

    override fun getFavUser(): Flow<PagingData<FavoriteUserEnt>> {
        return githubUserLocalDataSource.getFavUser()
    }

    override fun isFavorite(id: Int): Flow<Boolean> {
        return githubUserLocalDataSource.isFavorite(id)
    }
}