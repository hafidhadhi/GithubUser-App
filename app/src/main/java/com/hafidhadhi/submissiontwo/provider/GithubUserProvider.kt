package com.hafidhadhi.submissiontwo.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.hafidhadhi.submissiontwo.BuildConfig
import com.hafidhadhi.submissiontwo.data.local.AppDatabase
import com.hafidhadhi.submissiontwo.data.local.FavoriteUserDao
import com.hafidhadhi.submissiontwo.data.local.entity.toCursor
import com.hafidhadhi.submissiontwo.data.local.entity.toFavUserModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.components.ApplicationComponent

const val SCHEME = "content"
const val AUTHORITY = "${BuildConfig.APPLICATION_ID}.provider"
const val FAVORITE_TABLE = "favorite"
const val KEY_QUERY_PARAM_KEY = "key"
const val PER_PAGE_QUERY_PARAM_KEY = "per_page"
const val ID_QUERY_PARAM_KEY = "id"
val uriBuilder: Uri.Builder
    get() {
        return Uri.Builder()
            .scheme(SCHEME)
            .authority(AUTHORITY)
            .path(FAVORITE_TABLE)
    }

class GithubUserProvider : ContentProvider() {

    private lateinit var appDatabase: AppDatabase

    private lateinit var favoriteUserDao: FavoriteUserDao

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, FAVORITE_TABLE, 1)
    }

    override fun onCreate(): Boolean {
        val appContext = context!!.applicationContext
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            appContext,
            GithubUserProviderEntryPoint::class.java
        )
        appDatabase = hiltEntryPoint.appDatabase()
        favoriteUserDao = appDatabase.favoriteUserDao()
        return false
    }

    @Throws(IllegalArgumentException::class)
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        Log.d(this::class.simpleName, uri.toString())
        return when (uriMatcher.match(uri)) {
            1 -> {
                val key = uri.getQueryParameter(KEY_QUERY_PARAM_KEY)?.toLongOrNull()
                val perPage = uri.getQueryParameter(PER_PAGE_QUERY_PARAM_KEY)?.toIntOrNull()
                val data = favoriteUserDao.getFavUser(key, perPage)
                data.toCursor()
            }
            else -> throw IllegalArgumentException("Unknown Uri Path")
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.d(this::class.simpleName, uri.toString())
        return when (uriMatcher.match(uri)) {
            1 -> {
                favoriteUserDao.insertOrDeleteFavUser(
                    values?.toFavUserModel()
                        ?: throw IllegalArgumentException("Empty Content Value")
                )
                null
            }
            else -> throw IllegalArgumentException("Unknown Uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.d(this::class.simpleName, uri.toString())
        return when (uriMatcher.match(uri)) {
            1 -> {
                val id = uri.getQueryParameter(ID_QUERY_PARAM_KEY)?.toIntOrNull()
                    ?: throw IllegalArgumentException("Empty ID")
                favoriteUserDao.deleteFavUser(id)
                0
            }
            else -> throw IllegalArgumentException("Unknown Uri Path")
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface GithubUserProviderEntryPoint {
        fun appDatabase(): AppDatabase
    }
}