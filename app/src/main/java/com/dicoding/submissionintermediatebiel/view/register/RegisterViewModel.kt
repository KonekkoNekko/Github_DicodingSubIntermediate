package com.dicoding.submissionintermediatebiel.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.submissionintermediatebiel.data.api.ErrorResponse
import com.dicoding.submissionintermediatebiel.data.local.Repository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val repository: Repository): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isErrorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _isErrorMessage

    fun registerUser(name: String, email: String, password: String){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val registerResponse = repository.register(name, email, password)
                val message = registerResponse.message
                _isErrorMessage.value = message!!
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _isErrorMessage.value = errorMessage!!
            } finally {
                _isLoading.value = false
            }
        }
    }
}