package com.example.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.viewmodel.FavoriteViewModel
import com.example.githubuser.viewmodel.FavoriteViewModelFactory

class FavoriteActivity : AppCompatActivity(), UserAdapter.UserItemClickListener {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var favAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favAdapter = UserAdapter()
        favAdapter.notifyDataSetChanged()

        viewModel = ViewModelProvider(
            this,
            FavoriteViewModelFactory(application)
        ).get(FavoriteViewModel::class.java)

        binding.rvFav.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            adapter = favAdapter
        }

        viewModel.getAllFavorites().observe(this) { users ->
            val items = arrayListOf<ItemsItem>()
            users.map {
                val item = ItemsItem(login = it.login, avatarUrl = it.avatarUrl)
                items.add(item)
            }
            favAdapter.submitList(items)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClicked(user: ItemsItem) {
        Intent(this@FavoriteActivity, DetailActivity::class.java).also {
            it.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
            startActivity(it)
        }
    }
}
