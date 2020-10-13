package com.hafidhadhi.submissiontwo.data.local.pagingsource

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.paging.PagingSource
import com.hafidhadhi.consumerapp.data.local.FavoriteUser
import com.hafidhadhi.consumerapp.data.local.toFavUsersModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

const val SCHEME = "content"
const val AUTHORITY = "com.hafidhadhi.submissiontwo.provider"
const val FAVORITE_TABLE = "favorite"
const val KEY_QUERY_PARAM_KEY = "key"
const val PER_PAGE_QUERY_PARAM_KEY = "per_page"
val uriBuilder: Uri.Builder
    get() {
        return Uri.Builder()
            .scheme(SCHEME)
            .authority(AUTHORITY)
            .path(FAVORITE_TABLE)
    }

class FavoritePagingSource(
    private val context: Context,
    private val iODispatcher: CoroutineDispatcher
) :
    PagingSource<Long, FavoriteUser>() {
    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, FavoriteUser> {
        return withContext(iODispatcher) {
            try {
                val contentResolver = context.contentResolver
                val key: Long = params.key ?: -1L
                val uri = uriBuilder
                    .appendQueryParameter(KEY_QUERY_PARAM_KEY, key.toString())
                    .appendQueryParameter(PER_PAGE_QUERY_PARAM_KEY, 10.toString())
                    .build()
                val response = contentResolver.query(uri, null, null, null, null)
                val data = response?.toFavUsersModel() ?: emptyList()
                val lastUserCreatedAt = if (data.isNotEmpty()) data.last().createdAt else null
                Log.d(this::class.simpleName, data.toString())
                response?.close()
                LoadResult.Page(
                    data = data.map { it },
                    prevKey = null,
                    nextKey = lastUserCreatedAt
                )
            } catch (e: Exception) {
                Log.e(this::class.simpleName, e.message.toString(), e)
                LoadResult.Error(e)
            }
        }
    }

}