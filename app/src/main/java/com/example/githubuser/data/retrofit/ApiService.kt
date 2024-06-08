package com.example.githubuser.data.retrofit

import com.example.githubuser.data.response.DetailUserResponse
import com.example.githubuser.data.response.GithubResponse
import com.example.githubuser.data.response.ItemsItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users?q=")
    fun getUserSearch(
        @Query("q") id: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") login: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<ItemsItem>>
}