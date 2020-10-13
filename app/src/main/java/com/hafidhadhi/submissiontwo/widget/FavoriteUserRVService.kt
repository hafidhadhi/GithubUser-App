package com.hafidhadhi.submissiontwo.widget

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.hafidhadhi.submissiontwo.R
import com.hafidhadhi.submissiontwo.data.local.entity.FavoriteUserEnt
import com.hafidhadhi.submissiontwo.data.local.entity.toFavUsersModel
import com.hafidhadhi.submissiontwo.provider.uriBuilder

class FavoriteUserRVService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return FavoriteUserRVFactory()
    }

    inner class FavoriteUserRVFactory() : RemoteViewsFactory {

        private lateinit var contentResolver: ContentResolver
        private var favoriteUser = listOf<FavoriteUserEnt>()

        override fun onCreate() {
            contentResolver = applicationContext.contentResolver
        }

        override fun onDataSetChanged() {
            try {
                val identityToken = Binder.clearCallingIdentity();

                val uri = uriBuilder
                    .build()
                val response = contentResolver.query(uri, null, null, null, null)?.apply {
                    close()
                }
                favoriteUser = response?.toFavUsersModel() ?: emptyList()
                Binder.restoreCallingIdentity(identityToken)
            } catch (e: Exception) {
                favoriteUser = emptyList()
                Log.e(this::class.simpleName, e.message.toString(), e)
            }
        }

        override fun onDestroy() {}

        override fun getCount(): Int = favoriteUser.size

        override fun getViewAt(position: Int): RemoteViews {
            val favUser = favoriteUser.getOrNull(position)
            return RemoteViews(
                applicationContext.packageName,
                R.layout.favorite_user_widget_item
            ).apply {
                populateView(applicationContext, favUser)
            }
        }

        override fun getLoadingView(): RemoteViews? = null

        override fun getViewTypeCount(): Int = 1

        override fun getItemId(position: Int): Long = position.toLong()

        override fun hasStableIds(): Boolean = true

        private fun RemoteViews.populateView(context: Context, user: FavoriteUserEnt?) {
            setTextViewText(R.id.userName, user?.userName)
            setTextViewText(R.id.userId, context.getString(R.string.id_value, user?.id))
            val imageUrl = user?.avatarUrl
            imageUrl?.let {
                try {
                    val bitmap =
                        Glide.with(context.applicationContext).asBitmap().load(it).submit()
                    setImageViewBitmap(R.id.avatar, bitmap.get())
                } catch (e: Exception) {
                    setImageViewResource(R.id.avatar, R.drawable.ic_baseline_broken_image_24)
                    Log.e(this::class.simpleName, e.message.toString(), e)
                }
            }
        }
    }
}
