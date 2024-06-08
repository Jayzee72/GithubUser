package com.example.githubuser.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.adapter.SectionsPagerAdapter
import com.example.githubuser.data.local.FavoriteEntity
import com.example.githubuser.data.response.DetailUserResponse
import com.example.githubuser.databinding.ActivityDetailBinding
import com.example.githubuser.viewmodel.DetailViewModel
import com.example.githubuser.viewmodel.DetailViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var favoritesObserver: Observer<List<FavoriteEntity>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        val username = intent.getStringExtra("name") ?: ""
        sectionsPagerAdapter.username = username

        viewModel = ViewModelProvider(
            this,
            DetailViewModelFactory(application)
        ).get(DetailViewModel::class.java)

        if (username != null) {
            viewModel.getDetailUser(username)
        }

        viewModel.detailUser.observe(this) {
            setUser(it)
            checkFavoriteUser(it.login)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.fabFav.setOnClickListener {
            viewModel.detailUser.value?.let {
                if (viewModel.isUserFavorite) {
                    viewModel.removeFavorite(it.toFavoriteEntity())
                } else {
                    viewModel.addToFavorite(it.toFavoriteEntity())
                }
            }
        }

        favoritesObserver = Observer { favoriteEntities ->
        }
        viewModel.getAllFavorites().observe(this, favoritesObserver)

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.getAllFavorites().removeObserver(favoritesObserver)
    }

    private fun setUser(user: DetailUserResponse) {
        if (user != null) {
            binding.apply {
                tvDetailname.text = user.name ?: ""
                tvDetailusername.text = user.login ?: ""
                tvFollower.text = getString(R.string.follower, user.followers)
                tvFollowing.text = getString(R.string.following, user.following)
                Glide.with(this@DetailActivity)
                    .load(user.avatarUrl)
                    .into(ivDetailavatar)
                showLoading(false)
            }
        }
    }

    private fun checkFavoriteUser(username: String?) {
        viewModel.getUserFavoriteById(username).observe(this) { favoriteEntities ->
            viewModel.isUserFavorite = favoriteEntities.isNotEmpty()
            setFavoriteButtonIcon(viewModel.isUserFavorite)
        }
    }

    private fun setFavoriteButtonIcon(isFavorite: Boolean) {
        if (isFavorite) {
            binding.fabFav.setImageResource(R.drawable.favorite)
        } else {
            binding.fabFav.setImageResource(R.drawable.favorite_border)
        }
    }

    private fun DetailUserResponse.toFavoriteEntity(): FavoriteEntity {
        return FavoriteEntity(
            login = this.login ?: "",
            avatarUrl = this.avatarUrl
        )
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower_placeholder,
            R.string.following_placeholder
        )
    }

}

