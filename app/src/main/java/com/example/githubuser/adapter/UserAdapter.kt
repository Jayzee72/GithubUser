package com.example.githubuser.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.databinding.ItemUserBinding
import com.example.githubuser.ui.DetailActivity

class UserAdapter : ListAdapter<ItemsItem, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var userItemClickListener: UserItemClickListener

    fun setUserItemClickListener(listener: UserItemClickListener) {
        userItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        holder.itemView.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                val userName = user.login
                val photo = user.avatarUrl

                val intent = Intent(holder.itemView.context, DetailActivity::class.java)

                intent.putExtra("name", userName)
                intent.putExtra("photo", photo)

                holder.itemView.context.startActivity(intent)

            }
        }
    }

    class MyViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem) {
            binding.apply {
                tvUsername.text = user.login
                Glide.with(itemView.context)
                    .load(user.avatarUrl)
                    .into(imgAvatar)
            }

        }
    }

    interface UserItemClickListener {
        fun onItemClicked(user: ItemsItem)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}