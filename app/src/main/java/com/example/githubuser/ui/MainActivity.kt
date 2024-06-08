package com.example.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.controller.SettingPreferences
import com.example.githubuser.controller.dataStore
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.viewmodel.MainViewModel
import com.example.githubuser.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity(), UserAdapter.UserItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = adapter

        val pref = SettingPreferences.getInstance(application.dataStore)
        viewModel = ViewModelProvider(this, MainViewModelFactory(pref)).get(
            MainViewModel::class.java
        )

        viewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        viewModel.listUser.observe(this) { listUser ->
            setUserData(listUser)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    viewModel.searchUsers(searchView.text.toString())
                    searchView.hide()
                    Toast.makeText(this@MainActivity, searchView.text, Toast.LENGTH_SHORT).show()
                    false
                }
        }
        adapter.setUserItemClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_favlist -> {
                Intent(this, FavoriteActivity::class.java).apply {
                    startActivity(this)
                }
            }

            R.id.menu_setting -> {
                Intent(this, ThemeSettingActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClicked(user: ItemsItem) {
        Intent(this@MainActivity, DetailActivity::class.java).also {
            it.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
            startActivity(it)
        }
    }

    private fun setUserData(item: List<ItemsItem?>?) {
        adapter.submitList(item)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    /*private fun showError() {
        Toast.makeText(this, "Data tidak berhasil diambil",Toast.LENGTH_SHORT).show()
    }*/
}
