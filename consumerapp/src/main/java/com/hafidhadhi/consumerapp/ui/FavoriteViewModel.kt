package com.hafidhadhi.consumerapp.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hafidhadhi.consumerapp.data.local.FavoriteUser
import com.hafidhadhi.consumerapp.repository.GithubUserRepository
import kotlinx.coroutines.flow.Flow

class FavoriteViewModel @ViewModelInject constructor(private val githubUserRepository: GithubUserRepository) :
    ViewModel() {

    fun getFavUser(): Flow<PagingData<FavoriteUser>> {
        return githubUserRepository.getFavUser().cachedIn(viewModelScope)
    }
}