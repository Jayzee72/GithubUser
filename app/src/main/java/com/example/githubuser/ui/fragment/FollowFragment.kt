package com.example.githubuser.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.adapter.UserAdapter
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.viewmodel.FollowViewModel

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private lateinit var viewModel: FollowViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*inisialisasi viewmodel (dari detailviewmodel)*/
        viewModel = ViewModelProvider(this).get(FollowViewModel::class.java)

        // Inisialisasi RecyclerView
        binding.rvFollows.layoutManager = LinearLayoutManager(requireContext())

        arguments?.let {
            val position = it.getInt(ARG_POSITION)
            val username = it.getString(ARG_USERNAME)

            if (position == 1) {
                viewModel.getFollowers(username ?: "")
            } else {
                viewModel.getFollowing(username ?: "")
            }
        }

        viewModel.listFollower.observe(viewLifecycleOwner) { followerList ->
            setUserList(followerList)
        }

        viewModel.listFollowing.observe(viewLifecycleOwner) { followingList ->
            setUserList(followingList)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

    }

    private fun setUserList(listFollow: List<ItemsItem>) {
        adapter = UserAdapter()
        binding.rvFollows.adapter = adapter
        adapter.submitList(listFollow)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar5.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}
