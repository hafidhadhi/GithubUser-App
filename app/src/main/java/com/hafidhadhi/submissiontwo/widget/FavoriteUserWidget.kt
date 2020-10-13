package com.hafidhadhi.submissiontwo.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.hafidhadhi.submissiontwo.R

class FavoriteUserWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val rv = RemoteViews(context.packageName, R.layout.favorite_user_widget)
        rv.setEmptyView(R.id.stack_view, R.id.empty_view)
        val intent = Intent(context, FavoriteUserRVService::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
        rv.setRemoteAdapter(R.id.stack_view, intent)
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.stack_view)
        appWidgetManager.updateAppWidget(appWidgetId, rv)
    }
}