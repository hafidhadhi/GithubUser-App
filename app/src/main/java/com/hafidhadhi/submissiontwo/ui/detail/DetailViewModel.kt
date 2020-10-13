package com.hafidhadhi.submissiontwo.ui.detail

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.hafidhadhi.submissiontwo.data.remote.dto.GithubUser
import com.hafidhadhi.submissiontwo.repository.GithubUserRepository
import kotlinx.coroutines.launch

class DetailViewModel @ViewModelInject constructor(private val githubUserRepository: GithubUserRepository) :
    ViewModel() {
    private val _userName = MutableLiveData<String>()
    private val _isError = MutableLiveData<Exception>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _userData: LiveData<GithubUser> = _userName.switchMap {
        val liveData = MutableLiveData<GithubUser>()
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val data = githubUserRepository.getUser(it)
                liveData.value = data
            } catch (e: Exception) {
                _isError.value = e
                Log.e(this::class.java.simpleName, e.message.toString(), e)
            } finally {
                _isLoading.value = false
            }
        }
        liveData
    }

    val userData: LiveData<GithubUser> = _userData
    val isError: LiveData<Exception> = _isError
    val isLoading: LiveData<Boolean> = _isLoading
    fun isFavorite(id: Int): LiveData<Boolean> = githubUserRepository.isFavorite(id).asLiveData()

    fun getUser(userName: String) {
        _userName.value = userName
    }

    fun setFavorite(user: GithubUser) {
        viewModelScope.launch {
            try {
                githubUserRepository.insertOrDeleteFavUser(user)
            } catch (e: Exception) {
                _isError.value = e
                Log.e(this::class.java.simpleName, e.message.toString(), e)
            }
        }
    }
}