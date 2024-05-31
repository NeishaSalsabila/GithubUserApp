package com.neisha.githubuser.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.neisha.githubuser.database.FavoriteUser
import com.neisha.githubuser.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository = UserRepository(application)
    val allFavoriteUsers: LiveData<List<FavoriteUser>> = repository.getAllFavorite()

    fun insert(username: String, name: String?, avatarUrl: String, followersCount: String?, followingCount: String?) {
        val favoriteUser = FavoriteUser(
            username = username,
            name = name,
            avatarUrl = avatarUrl,
            followersCount = followersCount,
            followingCount = followingCount
        )
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(favoriteUser)
        }
    }


    fun delete(favoriteUser: FavoriteUser) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(favoriteUser)
        }
    }
}
