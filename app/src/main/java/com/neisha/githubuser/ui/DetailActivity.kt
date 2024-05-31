package com.neisha.githubuser.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.neisha.githubuser.R
import com.neisha.githubuser.data.response.DetailUserResponse
import com.neisha.githubuser.data.retrofit.ApiConfig
import com.neisha.githubuser.database.FavoriteUser
import com.neisha.githubuser.databinding.DetailActivityBinding
import com.neisha.githubuser.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: DetailActivityBinding
    private lateinit var viewPager: ViewPager2
    private lateinit var tabs: TabLayout
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var userRepository: UserRepository
    private var isFavorite: Boolean = false
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = binding.viewPager
        tabs = binding.tabs
        loadingIndicator = binding.progressBarDetail
        userRepository = UserRepository(application)
        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        fetchDetailUser(intent.getStringExtra("username") ?: "")
        setupViewPager()

        binding.fabFavorite.setOnClickListener {
            if (isFavorite) {
                userRepository.delete(
                    FavoriteUser(
                        username = intent.getStringExtra("username") ?: ""
                    )
                )
                binding.fabFavorite.setImageResource(R.drawable.favorite)
            } else {
                val favoriteUser = FavoriteUser(
                    username = intent.getStringExtra("username") ?: "",
                    name = binding.textViewName.text.toString(),
                    avatarUrl = intent.getStringExtra("avatarUrl")
                        ?: "", // Mengambil URL avatar dari intent
                    followersCount = binding.textViewFollowerCount.text.toString(),
                    followingCount = binding.textViewFollowingCount.text.toString()
                )
                userRepository.insert(favoriteUser)
                binding.fabFavorite.setImageResource(R.drawable.favorite_full)
            }
            isFavorite = !isFavorite
        }
    }

    private fun setupViewPager() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = intent.getStringExtra("username") ?: ""

        viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_followers)
                1 -> getString(R.string.tab_following)
                else -> ""
            }
        }.attach()
    }

    private fun fetchDetailUser(username: String) {
        loadingIndicator.visibility = View.VISIBLE
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val apiService = ApiConfig.getApiService()
                val response = apiService.getDetailUser(username)

                displayUserData(response)
                val avatarUrl = response.avatarUrl ?: ""
                isFavorite = userRepository.getIsFavorite(username)
                if (isFavorite) {
                    binding.fabFavorite.setImageResource(R.drawable.favorite_full)
                } else {
                    binding.fabFavorite.setImageResource(R.drawable.favorite)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle error here
            } finally {
                loadingIndicator.visibility = View.GONE
            }
        }
    }

    private fun displayUserData(detailUserResponse: DetailUserResponse) {
        binding.textViewName.text = detailUserResponse.name
        binding.textViewUsername.text = detailUserResponse.login
        binding.textViewFollowerCount.text = detailUserResponse.followers.toString()
        binding.textViewFollowingCount.text = detailUserResponse.following.toString()

        val avatarUrl = detailUserResponse.avatarUrl ?: ""
        loadAvatar(avatarUrl)
    }

    private fun loadAvatar(avatarUrl: String) {
        Glide.with(this)
            .load(avatarUrl)
            .transform(CircleCrop())
            .placeholder(R.drawable.ic_placeholder)
            .into(binding.imageViewAvatar)
    }
}