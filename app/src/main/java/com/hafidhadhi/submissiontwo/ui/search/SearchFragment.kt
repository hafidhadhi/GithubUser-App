package com.hafidhadhi.submissiontwo.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.hafidhadhi.submissiontwo.R
import com.hafidhadhi.submissiontwo.data.remote.dto.GithubUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), OnUserClickListener {

    private val searchViewModel by viewModels<SearchViewModel>()

    private var searchJob: Job? = null

    private val adapter = SearchUserAdapter(this)

    private var isInitialized = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRecyclerView()
        if (!isInitialized) {
            search("android")
            isInitialized = true
        }
    }

    private fun setupRecyclerView() {
        githubUserItems.adapter = adapter
    }

    private fun search(query: String) {
        searchJob?.cancel()
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.searchUser(query).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onUserClick(githubUser: GithubUser) {
        val directions = SearchFragmentDirections.actionSearchFragmentToDetailFragment(githubUser)
        findNavController().navigate(directions)
    }
}