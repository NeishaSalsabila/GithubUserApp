package com.neisha.githubuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neisha.githubuser.R
import com.neisha.githubuser.data.response.ItemsItem

class FollowerFollowingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var testUsername: TextView
    private lateinit var viewModel: FollowViewModel

    private var position: Int = 0
    private var username: String = ""

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"

        fun newInstance(position: Int, username: String): FollowerFollowingFragment {
            val fragment = FollowerFollowingFragment()
            val args = Bundle().apply {
                putInt(ARG_POSITION, position)
                putString(ARG_USERNAME, username)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME, "")
        }
        viewModel = ViewModelProvider(this).get(FollowViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_follower, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewDetail)
        progressBar = view.findViewById(R.id.progressBarDetail)
        testUsername = view.findViewById(R.id.testUsername)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (position == 1) {
            testUsername.text = "Get Follower $username"
            viewModel.fetchFollowers(username)
        } else {
            testUsername.text = "Get Following $username"
            viewModel.fetchFollowing(username)
        }
        progressBar.visibility = View.VISIBLE

        if (position == 1) {
            viewModel.followers.observe(viewLifecycleOwner, { followers: List<ItemsItem>? ->
                followers?.let {
                    val adapter = UserAdapter(it)
                    recyclerView.adapter = adapter
                }
                progressBar.visibility = View.GONE
            })
        } else {
            viewModel.following.observe(viewLifecycleOwner, { following: List<ItemsItem>? ->
                following?.let {
                    val adapter = UserAdapter(it)
                    recyclerView.adapter = adapter
                }
                progressBar.visibility = View.GONE
            })
        }
    }
}
