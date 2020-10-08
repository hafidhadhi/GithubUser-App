package com.hafidhadhi.submissiontwo.ui.detail.follower

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.hafidhadhi.submissiontwo.R
import com.hafidhadhi.submissiontwo.data.remote.dto.GithubUser
import com.hafidhadhi.submissiontwo.ui.detail.DetailFragment.Companion.EXTRA_GITHUB_USER
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_follower.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowerFragment : Fragment() {

    private val followerViewModel by viewModels<FollowerViewModel>()

    private var getFollowersJob: Job? = null

    private val adapter = FollowerAdapter()

    private var isInitialized = false

    private var githubUser: GithubUser = GithubUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments?.getParcelable<GithubUser>(EXTRA_GITHUB_USER)
        args?.let {
            githubUser = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupAdapter()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        if (!isInitialized) {
            githubUser.userName?.let {
                getFollowers(it)
            }
            isInitialized = true
        }
    }

    private fun setupAdapter() {
        adapter.addLoadStateListener { loadState ->
            progressBar.isVisible = loadState.append is LoadState.Loading ||
                    loadState.refresh is LoadState.Loading ||
                    loadState.prepend is LoadState.Loading

            val errorState = loadState.refresh as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    requireContext(),
                    "${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setupRecyclerView() {
        followerItems.adapter = adapter
    }

    private fun getFollowers(query: String) {
        getFollowersJob?.cancel()
        getFollowersJob = viewLifecycleOwner.lifecycleScope.launch {
            followerViewModel.getFollowers(query).collectLatest {
                adapter.submitData(it)
            }
        }
    }
}