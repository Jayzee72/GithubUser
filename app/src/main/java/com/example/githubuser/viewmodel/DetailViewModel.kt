package com.example.githubuser.viewmodel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.local.FavoriteEntity
import com.example.githubuser.data.repository.FavoriteRepository
import com.example.githubuser.data.response.DetailUserResponse
import com.example.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailViewModel(application: Application) : ViewModel() {
    private val favoriteRepository: FavoriteRepository = FavoriteRepository(application)

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    var isUserFavorite = false

    fun getDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getUserFavoriteById(username: String?): LiveData<List<FavoriteEntity>> =
        favoriteRepository.getUserFavoriteById(username ?: "")

    fun addToFavorite(favoriteEntity: FavoriteEntity) {
        if (favoriteEntity.login.isNotEmpty()) {
            favoriteRepository.insert(favoriteEntity)
            isUserFavorite = true
        }
    }

    fun removeFavorite(favoriteEntity: FavoriteEntity) {
        if (favoriteEntity.login.isNotEmpty()) {
            favoriteRepository.delete(favoriteEntity)
            isUserFavorite = false
        }
    }

    fun getAllFavorites(): LiveData<List<FavoriteEntity>> = favoriteRepository.getAllFavorites()


}
