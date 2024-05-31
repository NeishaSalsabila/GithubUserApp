package com.neisha.githubuser.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.neisha.githubuser.R
import com.neisha.githubuser.database.FavoriteUser
import com.neisha.githubuser.databinding.ItemUserBinding

class FavoriteAdapter(private var favoriteUsers: List<FavoriteUser>) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private var onItemClickListener: ((FavoriteUser) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val user = favoriteUsers[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return favoriteUsers.size
    }

    fun setData(users: List<FavoriteUser>) {
        favoriteUsers = users
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (FavoriteUser) -> Unit) {
        onItemClickListener = listener
    }

    inner class FavoriteViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteUser: FavoriteUser) {
            // Cetak URL avatar untuk memastikan data tersedia
            Log.d("AvatarURL", "Avatar URL: ${favoriteUser.avatarUrl}")

            binding.textViewUsername.text = favoriteUser.username
            if (!favoriteUser.avatarUrl.isNullOrBlank()) {
                Glide.with(binding.root.context)
                    .load(favoriteUser.avatarUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .transform(CircleCrop())
                    .into(binding.imageViewAvatar)
            } else {
                Glide.with(binding.root.context)
                    .load(R.drawable.ic_placeholder)
                    .transform(CircleCrop())
                    .into(binding.imageViewAvatar)
            }

            binding.root.setOnClickListener {
                onItemClickListener?.invoke(favoriteUser)
            }
        }
    }
}