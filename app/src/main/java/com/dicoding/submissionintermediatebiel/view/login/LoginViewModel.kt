package com.dicoding.submissionintermediatebiel.view.login

import android.util.Log
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

    suspend fun loginUser(email: String, password: String): LoginResponse {
        _isLoading.value = true
        val response = repository.login(email, password)
        _isLoading.value = false
        return response
    }

    suspend fun saveSession(userModel: UserModel): Boolean {
        Log.d("SaveSession() LoginViewModel", true.toString())
        return runCatching {
            repository.saveSession(userModel)
            true // Return true if the session is saved successfully
        }.getOrElse {
            // Handle the error, log it, and return false
            Log.e("SaveSession() LoginViewModel", "Failed to save session", it)
            false
        }
    }
}
