package com.neisha.githubuser.data.retrofit

import com.neisha.githubuser.data.response.DetailUserResponse
import com.neisha.githubuser.data.response.GithubResponse
import com.neisha.githubuser.data.response.ItemsItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun getAllUsers(): List<ItemsItem>

    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") username: String,
        @Query("per_page") perPage: Int = 100
    ): GithubResponse

    @GET("users/{username}")
    suspend fun getDetailUser(@Path("username") username: String): DetailUserResponse

    @GET("users/{username}/followers")
    suspend fun getFollowers(@Path("username") username: String): List<ItemsItem>

    @GET("users/{username}/following")
    suspend fun getFollowing(@Path("username") username: String): List<ItemsItem>
}
