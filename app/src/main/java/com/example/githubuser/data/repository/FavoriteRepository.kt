package com.example.githubuser.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuser.data.local.FavoriteDao
import com.example.githubuser.data.local.FavoriteEntity
import com.example.githubuser.data.local.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val favDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        favDao = db.favDao()
    }

    fun getAllFavorites(): LiveData<List<FavoriteEntity>> = favDao.getAllFavorite()

    fun getUserFavoriteById(login: String): LiveData<List<FavoriteEntity>> =
        favDao.getUserFavoriteById(login)

    fun insert(fav: FavoriteEntity) {
        executorService.execute { favDao.insert(fav) }
    }

    fun delete(fav: FavoriteEntity) {
        executorService.execute { favDao.delete(fav) }
    }

}