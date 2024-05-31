package com.neisha.githubuser.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neisha.githubuser.data.response.ItemsItem
import com.neisha.githubuser.databinding.ItemUserBinding

class UserAdapter(var users: List<ItemsItem?>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var onItemClick: ((ItemsItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (ItemsItem) -> Unit) {
        onItemClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        user?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem) {
            binding.apply {
                textViewUsername.text = user.login
                Glide.with(root)
                    .load(user.avatarUrl)
                    .circleCrop()
                    .into(imageViewAvatar)

                itemView.setOnClickListener {
                    onItemClick?.invoke(user)
                }
            }
        }
    }
}
