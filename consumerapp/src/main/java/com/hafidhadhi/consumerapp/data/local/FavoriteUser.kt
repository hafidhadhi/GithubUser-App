package com.hafidhadhi.consumerapp.data.local

import android.database.Cursor
import android.database.MatrixCursor
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import java.util.*

const val ID_COLUMN = "id"
const val USERNAME_COLUMN = "username"
const val NAME_COLUMN = "name"
const val AVATAR_URL_COLUMN = "avatar_url"
const val PROFILE_URL_COLUMN = "profile_url"
const val COMPANY_COLUMN = "company"
const val LOCATION_COLUMN = "location"
const val REPOS_COLUMN = "repos"
const val FOLLOWERS_COLUMN = "followers"
const val FOLLOWING_COLUMN = "following"
const val CREATED_AT_COLUMN = "created_at"

data class FavoriteUser(
    val id: Int = 0,
    val userName: String? = null,
    val name: String? = null,
    val avatarUrl: String? = null,
    val profileUrl: String? = null,
    val company: String? = null,
    val location: String? = null,
    val repos: Int? = null,
    val followers: Int? = null,
    val following: Int? = null,
    val createdAt: Long = Calendar.getInstance().timeInMillis
)

fun List<FavoriteUser>.toCursor(): Cursor {
    val columns = arrayOf(
        ID_COLUMN,
        USERNAME_COLUMN,
        NAME_COLUMN,
        AVATAR_URL_COLUMN,
        PROFILE_URL_COLUMN,
        COMPANY_COLUMN,
        LOCATION_COLUMN,
        REPOS_COLUMN,
        FOLLOWERS_COLUMN,
        FOLLOWING_COLUMN,
        CREATED_AT_COLUMN
    )
    return MatrixCursor(columns).apply {
        this@toCursor.forEach { user ->
            addRow(
                arrayOf(
                    user.id,
                    user.userName,
                    user.name,
                    user.avatarUrl,
                    user.profileUrl,
                    user.company,
                    user.location,
                    user.repos,
                    user.followers,
                    user.following,
                    user.createdAt
                )
            )
        }
    }
}

fun Cursor.toFavUsersModel(): List<FavoriteUser> {
    val data = mutableListOf<FavoriteUser>()
    return run {
        while (moveToNext()) {
            data.add(
                position, FavoriteUser(
                    id = getIntOrNull(getColumnIndex(ID_COLUMN)) ?: 0,
                    userName = getStringOrNull(getColumnIndex(USERNAME_COLUMN)),
                    name = getStringOrNull(getColumnIndex(NAME_COLUMN)),
                    avatarUrl = getStringOrNull(getColumnIndex(AVATAR_URL_COLUMN)),
                    profileUrl = getStringOrNull(getColumnIndex(PROFILE_URL_COLUMN)),
                    company = getStringOrNull(getColumnIndex(COMPANY_COLUMN)),
                    location = getStringOrNull(getColumnIndex(LOCATION_COLUMN)),
                    repos = getIntOrNull(getColumnIndex(REPOS_COLUMN)),
                    followers = getIntOrNull(getColumnIndex(FOLLOWERS_COLUMN)),
                    following = getIntOrNull(getColumnIndex(FOLLOWING_COLUMN)),
                    createdAt = getLongOrNull(getColumnIndex(CREATED_AT_COLUMN)) ?: -1
                )
            )
        }
        data
    }
}