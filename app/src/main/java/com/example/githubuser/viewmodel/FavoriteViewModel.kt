package com.example.githubuser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.githubuser.data.local.FavoriteEntity
import com.example.githubuser.data.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FavoriteRepository = FavoriteRepository(application)

    fun getAllFavorites(): LiveData<List<FavoriteEntity>> = repository.getAllFavorites()
}