package com.hafidhadhi.submissiontwo.di

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import androidx.room.Room
import com.hafidhadhi.submissiontwo.BuildConfig
import com.hafidhadhi.submissiontwo.data.GithubUserDataSource
import com.hafidhadhi.submissiontwo.data.local.AppDatabase
import com.hafidhadhi.submissiontwo.data.local.GithubUserLocalDataSource
import com.hafidhadhi.submissiontwo.data.remote.GithubService
import com.hafidhadhi.submissiontwo.data.remote.GithubUserRemoteDataSource
import com.hafidhadhi.submissiontwo.data.remote.utils.MyInterceptor
import com.hafidhadhi.submissiontwo.repository.GithubUserRepository
import com.hafidhadhi.submissiontwo.repository.GithubUserRepositoryImpl
import com.hafidhadhi.submissiontwo.util.createChannel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class GithubUserRemoteDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class GithubUserLocalDataSource

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
    @GithubUserRemoteDataSource
    @Provides
    fun provideGithubUserRemoteDataSource(
        retrofit: Retrofit,
        moshi: Moshi,
        @Named("Dispatchers.IO") iODispatcher: CoroutineDispatcher,
        @Named("Dispatchers.Default") defDispatcher: CoroutineDispatcher
    ): GithubUserDataSource =
        GithubUserRemoteDataSource(
            retrofit.create(GithubService::class.java),
            moshi,
            iODispatcher,
            defDispatcher
        )

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "github.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @GithubUserLocalDataSource
    @Provides
    fun provideGithubUserLocalDataSource(
        appDatabase: AppDatabase,
        @ApplicationContext context: Context,
        @Named("Dispatchers.IO") iODispatcher: CoroutineDispatcher
    ): GithubUserDataSource =
        GithubUserLocalDataSource(appDatabase.favoriteUserDao(), context, iODispatcher)

    @Singleton
    @Provides
    @Named("Dispatchers.IO")
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    @Named("Dispatchers.Default")
    fun provideDefDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Singleton
    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager =
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
            createChannel(context)
        }

    @Singleton
    @Provides
    fun provideAlarmManager(@ApplicationContext context: Context): AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
}

@Module
@InstallIn(ApplicationComponent::class)
abstract class AppModuleBinds {

    @Singleton
    @Binds
    abstract fun bindGithubUserRepository(impl: GithubUserRepositoryImpl): GithubUserRepository
}