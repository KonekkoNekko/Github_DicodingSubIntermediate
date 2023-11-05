package com.dicoding.submissionintermediatebiel.view.storylist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.submissionintermediatebiel.data.api.ErrorResponse
import com.dicoding.submissionintermediatebiel.data.api.ListStoryItem
import com.dicoding.submissionintermediatebiel.data.local.Repository
import com.dicoding.submissionintermediatebiel.data.pref.UserModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class StoryListViewModel(private val repository: Repository): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isErrorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _isErrorMessage

    private val _listStories = MutableLiveData<List<ListStoryItem>>()
    val listStories: LiveData<List<ListStoryItem>> = _listStories

    init {
        viewModelScope.launch {
            getStories()
        }
    }
    fun getSession(): LiveData<UserModel>{
        return repository.getSession().asLiveData()
    }

    private suspend fun getStories(){
        _isLoading.value = true
        val storyResponse = repository.getStories()
        val message = storyResponse.message
        try {
            _listStories.value = storyResponse.listStory
            _isErrorMessage.value = message!!
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Log.d("StoryListViewModel", errorMessage.toString())
            _isErrorMessage.value = errorMessage!!
        } finally {
            _isLoading.value = false
        }
    }
}