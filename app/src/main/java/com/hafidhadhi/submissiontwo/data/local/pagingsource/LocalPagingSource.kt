package com.hafidhadhi.submissiontwo.data.local.pagingsource

import android.content.Context
import android.util.Log
import androidx.paging.PagingSource
import com.hafidhadhi.submissiontwo.data.local.entity.FavoriteUserEnt
import com.hafidhadhi.submissiontwo.data.local.entity.toFavUsersModel
import com.hafidhadhi.submissiontwo.provider.KEY_QUERY_PARAM_KEY
import com.hafidhadhi.submissiontwo.provider.PER_PAGE_QUERY_PARAM_KEY
import com.hafidhadhi.submissiontwo.provider.uriBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FavoritePagingSource(
    private val context: Context,
    private val iODispatcher: CoroutineDispatcher
) :
    PagingSource<Long, FavoriteUserEnt>() {
    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, FavoriteUserEnt> {
        return withContext(iODispatcher) {
            try {
                val contentResolver = context.contentResolver
                val key: Long = params.key ?: -1L
                val uri = uriBuilder
                    .appendQueryParameter(KEY_QUERY_PARAM_KEY, key.toString())
                    .appendQueryParameter(PER_PAGE_QUERY_PARAM_KEY, 10.toString())
                    .build()
                val response = contentResolver.query(uri, null, null, null, null)?.apply {
                    close()
                }
                val data = response?.toFavUsersModel() ?: emptyList()
                val lastUserCreatedAt = if (data.isNotEmpty()) data.last().createdAt else null
                Log.d(this::class.simpleName, data.toString())
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