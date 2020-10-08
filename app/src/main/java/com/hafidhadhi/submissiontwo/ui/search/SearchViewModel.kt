package com.hafidhadhi.submissiontwo.ui.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hafidhadhi.submissiontwo.data.remote.dto.GithubUser
import com.hafidhadhi.submissiontwo.repository.GithubUserRepository
import kotlinx.coroutines.flow.Flow

class SearchViewModel @ViewModelInject constructor(private val githubUserRepository: GithubUserRepository) :
    ViewModel() {

    private var name: String? = null
    private var currentSearchResult: Flow<PagingData<GithubUser>>? = null
    fun searchUser(name: String): Flow<PagingData<GithubUser>> {
        val lastResult = currentSearchResult
        if (name == this.name && lastResult != null) return lastResult
        val newResult = githubUserRepository.searchUser(name).cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}