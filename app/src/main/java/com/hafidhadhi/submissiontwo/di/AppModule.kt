package com.hafidhadhi.submissiontwo.di

import com.hafidhadhi.submissiontwo.BuildConfig
import com.hafidhadhi.submissiontwo.data.remote.GithubService
import com.hafidhadhi.submissiontwo.data.remote.GithubUserRemoteDataSource
import com.hafidhadhi.submissiontwo.data.remote.MyInterceptor
import com.hafidhadhi.submissiontwo.repository.GithubUserRepository
import com.hafidhadhi.submissiontwo.repository.GithubUserRepositoryImpl
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .apply {
            addInterceptor(MyInterceptor())
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
            connectTimeout(3, TimeUnit.SECONDS)
            readTimeout(3, TimeUnit.SECONDS)
        }
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideGithubUserRemoteDataSource(
        retrofit: Retrofit,
        moshi: Moshi
    ): GithubUserRemoteDataSource =
        GithubUserRemoteDataSource(retrofit.create(GithubService::class.java), moshi)
}

@Module
@InstallIn(ApplicationComponent::class)
abstract class AppModuleBinds {
    @Binds
    abstract fun bindGithubUserRepository(impl: GithubUserRepositoryImpl): GithubUserRepository
}