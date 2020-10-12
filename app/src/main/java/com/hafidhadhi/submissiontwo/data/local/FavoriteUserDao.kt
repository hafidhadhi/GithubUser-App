package com.hafidhadhi.submissiontwo.data.local

import androidx.room.*
import com.hafidhadhi.submissiontwo.data.local.entity.FavoriteUserEnt
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertFavUser(favUser: FavoriteUserEnt)

    @Query("DELETE FROM favorite_user WHERE id = :id")
    abstract fun deleteFavUser(id: Int)

    @Query("SELECT * FROM favorite_user WHERE created_at > :key ORDER BY created_at LIMIT :perPage")
    abstract fun getFavUser(key: Long? = -1, perPage: Int? = 0): List<FavoriteUserEnt>

    @Query("SELECT * FROM favorite_user WHERE id = :id")
    abstract fun isFavorite(id: Int): Flow<List<FavoriteUserEnt>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertFavUserIgnorable(favUser: FavoriteUserEnt): Long

    @Transaction
    open fun insertOrDeleteFavUser(favUser: FavoriteUserEnt) {
        val rowId = insertFavUserIgnorable(favUser)
        if (rowId == -1L) {
            deleteFavUser(favUser.id)
        }
    }
}