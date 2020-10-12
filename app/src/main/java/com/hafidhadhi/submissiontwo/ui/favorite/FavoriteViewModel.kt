package com.hafidhadhi.submissiontwo.ui.favorite

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hafidhadhi.submissiontwo.data.local.entity.FavoriteUserEnt
import com.hafidhadhi.submissiontwo.repository.GithubUserRepository
import kotlinx.coroutines.flow.Flow

class FavoriteViewModel @ViewModelInject constructor(private val githubUserRepository: GithubUserRepository) :
    ViewModel() {

    fun getFavUser(): Flow<PagingData<FavoriteUserEnt>> {
        return githubUserRepository.getFavUser().cachedIn(viewModelScope)
    }
}