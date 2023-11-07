package com.dicoding.submissionintermediatebiel.view.features

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.submissionintermediatebiel.data.api.UploadStoryResponse
import com.dicoding.submissionintermediatebiel.data.local.Repository
import com.dicoding.submissionintermediatebiel.data.pref.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: Repository): ViewModel() {
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    suspend fun uploadFile(file: MultipartBody.Part, description: RequestBody): UploadStoryResponse{
        return repository.uploadImage(file, description)
    }
}