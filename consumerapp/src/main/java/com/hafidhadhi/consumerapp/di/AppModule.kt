package com.hafidhadhi.consumerapp.di

import android.content.Context
import com.hafidhadhi.consumerapp.data.GithubUserDataSource
import com.hafidhadhi.consumerapp.data.local.GithubUserLocalDataSource
import com.hafidhadhi.consumerapp.repository.GithubUserRepository
import com.hafidhadhi.consumerapp.repository.GithubUserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGithubUserLocalDataSource(
        @ApplicationContext context: Context,
        iODispatcher: CoroutineDispatcher
    ): GithubUserDataSource =
        GithubUserLocalDataSource(context, iODispatcher)

    @Singleton
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
}

@Module
@InstallIn(ApplicationComponent::class)
abstract class AppModuleBinds {

    @Singleton
    @Binds
    abstract fun bindGithubUserRepository(impl: GithubUserRepositoryImpl): GithubUserRepository
}