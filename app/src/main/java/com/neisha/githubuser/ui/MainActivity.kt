package com.neisha.githubuser.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.neisha.githubuser.data.response.ItemsItem
import com.neisha.githubuser.data.retrofit.ApiConfig
import com.neisha.githubuser.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    private var userList = listOf<ItemsItem?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Set up layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView, fetch data, and search view
        setupRecyclerView()
        fetchData("")
        setupSearchView()

        // Set up favorite button
        binding.ivFavorite.setOnClickListener {
            val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
            startActivity(intent)
        }
        binding.ivSettings.setOnClickListener {
            val intent = Intent(this@MainActivity, SettingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(this)
        userAdapter = UserAdapter(userList)
        binding.recyclerViewUsers.adapter = userAdapter

        userAdapter.setOnItemClickListener { user ->
            val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
                putExtra("username", user.login)
                putExtra("avatarUrl", user.avatarUrl)
            }
            startActivity(intent)
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    fetchData(it)
                    hideKeyboard()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun fetchData(query: String) {
        binding.loadingIndicator.visibility = View.VISIBLE

        GlobalScope.launch(Dispatchers.Main) {
            val apiService = ApiConfig.getApiService()
            val response = if (query.isNotBlank()) {
                apiService.searchUsers(query).items ?: emptyList<ItemsItem>()
            } else {
                apiService.getAllUsers() ?: emptyList<ItemsItem>()
            }

            binding.loadingIndicator.visibility = View.GONE

            userAdapter.users = response
            userAdapter.notifyDataSetChanged()
        }
    }
}
