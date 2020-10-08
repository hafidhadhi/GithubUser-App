package com.hafidhadhi.submissiontwo.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.hafidhadhi.submissiontwo.R
import com.hafidhadhi.submissiontwo.data.remote.dto.GithubUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), OnUserClickListener, SearchView.OnQueryTextListener {

    private val searchViewModel by viewModels<SearchViewModel>()

    private var searchJob: Job? = null

    private val adapter = SearchUserAdapter(this)

    private var isInitialized = false

    companion object {
        const val EXTRA_USER_NAME = "EXTRA_USER_NAME"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initialProc(savedInstanceState)
        setupSearchBar()
        setupAdapter()
        setupRecyclerView()
    }


    private fun initialProc(savedInstanceState: Bundle?) {
        val userName = searchViewModel.currentName.value
        Log.d(this::class.simpleName, userName.toString())
        userName.takeIf { !it.isNullOrEmpty() || !it.isNullOrBlank() }?.let {
            search(it)
        }
    }

    private fun setupSearchBar() {
        val searchBar = searchBar
        searchBar.apply {
            setOnQueryTextListener(this@SearchFragment)
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
        githubUserItems.adapter = adapter
    }

    private fun search(query: String, forceUpdate: Boolean = false) {
        searchJob?.cancel()
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            searchViewModel.searchUser(query, forceUpdate).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onUserClick(githubUser: GithubUser) {
        val directions = SearchFragmentDirections.actionSearchFragmentToDetailFragment(githubUser)
        findNavController().navigate(directions)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query.takeIf { !it.isNullOrEmpty() || !it.isNullOrBlank() }?.let {
            search(it, true)
            githubUserItems.smoothScrollToPosition(0)
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }
}