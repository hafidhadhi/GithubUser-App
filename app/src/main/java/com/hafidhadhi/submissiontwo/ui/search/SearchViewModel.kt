package com.hafidhadhi.submissiontwo.ui.search

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hafidhadhi.submissiontwo.data.remote.dto.GithubUser
import com.hafidhadhi.submissiontwo.repository.GithubUserRepository
import kotlinx.coroutines.flow.Flow

class SearchViewModel @ViewModelInject constructor(private val githubUserRepository: GithubUserRepository) :
    ViewModel() {

    private var _currentName = MutableLiveData<String>()
    private var _currentSearchResult: Flow<PagingData<GithubUser>>? = null
    val currentName: LiveData<String> = _currentName
    fun searchUser(name: String, forceUpdate: Boolean = false): Flow<PagingData<GithubUser>> {
        val lastResult = _currentSearchResult
        if (name == this._currentName.value && lastResult != null && !forceUpdate) return lastResult
        _currentName.value = name
        val newResult = githubUserRepository.searchUser(name).cachedIn(viewModelScope)
        _currentSearchResult = newResult
        return newResult
    }
}