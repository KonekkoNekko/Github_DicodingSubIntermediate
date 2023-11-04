package com.dicoding.submissionintermediatebiel.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.submissionintermediatebiel.data.api.LoginResponse
import com.dicoding.submissionintermediatebiel.data.local.Repository
import com.dicoding.submissionintermediatebiel.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isErrorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _isErrorMessage

    suspend fun loginUser(email: String, password: String): LoginResponse{
        _isLoading.value = true
        val response = repository.login(email, password)
        _isLoading.value = false
        return response
    }

    fun saveSession(userModel: UserModel){
        viewModelScope.launch {
            repository.saveSession(userModel)
        }
    }
}