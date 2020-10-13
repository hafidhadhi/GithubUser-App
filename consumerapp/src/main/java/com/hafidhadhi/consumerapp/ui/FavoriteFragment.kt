package com.hafidhadhi.consumerapp.ui

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
import com.hafidhadhi.consumerapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private val favoriteViewModel by viewModels<FavoriteViewModel>()

    private var getFavUserJob: Job? = null

    private val adapter = FavoriteAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupAdapter()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        initialProc()
    }

    private fun initialProc() {
        getFavUserJob?.cancel()
        getFavUserJob = viewLifecycleOwner.lifecycleScope.launch {
            favoriteViewModel.getFavUser().collectLatest {
                adapter.submitData(it)
            }
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
        favoriteItems.adapter = adapter
    }
}