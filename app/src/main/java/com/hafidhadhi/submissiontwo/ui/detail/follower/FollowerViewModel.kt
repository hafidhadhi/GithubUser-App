package com.hafidhadhi.submissiontwo.ui.detail.follower

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hafidhadhi.submissiontwo.data.remote.dto.GithubUser
import com.hafidhadhi.submissiontwo.repository.GithubUserRepository
import kotlinx.coroutines.flow.Flow

class FollowerViewModel @ViewModelInject constructor(private val githubUserRepository: GithubUserRepository) :
    ViewModel() {

    private var currentName: String? = null
    private var currentSearchResult: Flow<PagingData<GithubUser>>? = null
    fun getFollowers(name: String): Flow<PagingData<GithubUser>> {
        val lastResult = currentSearchResult
        if (name == this.currentName && lastResult != null) return lastResult
        currentName = name
        val newResult = githubUserRepository.getFollowers(name).cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}