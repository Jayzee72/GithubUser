package com.example.githubuser.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(fav: FavoriteEntity)

    @Update
    fun update(fav: FavoriteEntity)

    @Delete
    fun delete(fav: FavoriteEntity)

    @Query("SELECT  * from list_favorite")
    fun getAllFavorite(): LiveData<List<FavoriteEntity>>

    @Query("SELECT  * from list_favorite WHERE login = :login")
    fun getUserFavoriteById(login: String): LiveData<List<FavoriteEntity>>
}