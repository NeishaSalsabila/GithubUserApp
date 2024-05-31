package com.neisha.githubuser.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.neisha.githubuser.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        favoriteAdapter = FavoriteAdapter(emptyList())

        binding.rvUserFavorite.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
        }

        favoriteViewModel.allFavoriteUsers.observe(this, { favoriteUsers ->
            favoriteAdapter.setData(favoriteUsers)
        })

        favoriteAdapter.setOnItemClickListener { favoriteUser ->
            val intent = Intent(this@FavoriteActivity, DetailActivity::class.java).apply {
                putExtra("username", favoriteUser.username)
                putExtra("avatarUrl", favoriteUser.avatarUrl)
                //Saya ingin meminta bantuan, karena saya tidak bisa menampilkan AvatarUrl di halaman FavoriteActivity
            }
            startActivity(intent)
        }
    }
}