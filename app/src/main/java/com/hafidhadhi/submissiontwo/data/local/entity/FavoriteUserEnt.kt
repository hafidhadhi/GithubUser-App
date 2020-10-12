package com.hafidhadhi.submissiontwo.data.local.entity

import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hafidhadhi.submissiontwo.data.remote.dto.GithubUser
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

@Entity(tableName = "favorite_user")
data class FavoriteUserEnt(
    @PrimaryKey @ColumnInfo(name = ID_COLUMN) val id: Int = 0,
    @ColumnInfo(name = USERNAME_COLUMN) val userName: String? = null,
    @ColumnInfo(name = NAME_COLUMN) val name: String? = null,
    @ColumnInfo(name = AVATAR_URL_COLUMN) val avatarUrl: String? = null,
    @ColumnInfo(name = PROFILE_URL_COLUMN) val profileUrl: String? = null,
    @ColumnInfo(name = COMPANY_COLUMN) val company: String? = null,
    @ColumnInfo(name = LOCATION_COLUMN) val location: String? = null,
    @ColumnInfo(name = REPOS_COLUMN) val repos: Int? = null,
    @ColumnInfo(name = FOLLOWERS_COLUMN) val followers: Int? = null,
    @ColumnInfo(name = FOLLOWING_COLUMN) val following: Int? = null,
    @ColumnInfo(name = CREATED_AT_COLUMN) val createdAt: Long = Calendar.getInstance().timeInMillis
)

fun List<FavoriteUserEnt>.toCursor(): Cursor {
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

fun Cursor.toFavUsersModel(): List<FavoriteUserEnt> {
    val data = mutableListOf<FavoriteUserEnt>()
    return run {
        while (moveToNext()) {
            data.add(
                position, FavoriteUserEnt(
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

fun FavoriteUserEnt.toContentValues(): ContentValues {
    return ContentValues().apply {
        put(ID_COLUMN, id)
        put(USERNAME_COLUMN, userName)
        put(NAME_COLUMN, name)
        put(AVATAR_URL_COLUMN, avatarUrl)
        put(PROFILE_URL_COLUMN, profileUrl)
        put(COMPANY_COLUMN, company)
        put(LOCATION_COLUMN, location)
        put(REPOS_COLUMN, repos)
        put(FOLLOWERS_COLUMN, followers)
        put(FOLLOWING_COLUMN, following)
        put(CREATED_AT_COLUMN, createdAt)
    }
}

fun ContentValues.toFavUserModel(): FavoriteUserEnt {
    return FavoriteUserEnt(
        id = getAsInteger(ID_COLUMN),
        userName = getAsString(USERNAME_COLUMN),
        name = getAsString(NAME_COLUMN),
        avatarUrl = getAsString(AVATAR_URL_COLUMN),
        profileUrl = getAsString(PROFILE_URL_COLUMN),
        company = getAsString(COMPANY_COLUMN),
        location = getAsString(LOCATION_COLUMN),
        repos = getAsInteger(REPOS_COLUMN),
        followers = getAsInteger(FOLLOWERS_COLUMN),
        following = getAsInteger(FOLLOWING_COLUMN),
        createdAt = getAsLong(CREATED_AT_COLUMN)
    )
}

fun FavoriteUserEnt.toGithubUserModel(): GithubUser {
    return GithubUser(
        id, userName, name, avatarUrl, profileUrl, company, location, repos, followers, following
    )
}