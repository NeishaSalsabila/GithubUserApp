package com.neisha.githubuser.ui

import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neisha.githubuser.data.response.ItemsItem
import com.neisha.githubuser.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FollowViewModel : ViewModel() {

    private val apiService = ApiConfig.getApiService()

    private val _followers = MutableLiveData<List<ItemsItem>>()
    val followers: LiveData<List<ItemsItem>> = _followers

    private val _following = MutableLiveData<List<ItemsItem>>()
    val following: LiveData<List<ItemsItem>> = _following

    private var loadingIndicator: ProgressBar? = null

    fun setLoadingIndicator(progressBar: ProgressBar) {
        loadingIndicator = progressBar
    }

    fun fetchFollowers(username: String) {
        loadingIndicator?.visibility = View.VISIBLE
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getFollowers(username)
                _followers.postValue(response)
            } catch (e: Exception) {
            } finally {
                loadingIndicator?.visibility = View.GONE
            }
        }
    }

    fun fetchFollowing(username: String) {
        loadingIndicator?.visibility = View.VISIBLE
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getFollowing(username)
                _following.postValue(response)
            } catch (e: Exception) {
            } finally {
                loadingIndicator?.visibility = View.GONE
            }
        }
    }
}
