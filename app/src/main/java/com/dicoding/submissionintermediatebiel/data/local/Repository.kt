package com.dicoding.submissionintermediatebiel.data.local

import android.util.Log
import com.dicoding.submissionintermediatebiel.data.api.ApiService
import com.dicoding.submissionintermediatebiel.data.api.FetchAllStoryResponse
import com.dicoding.submissionintermediatebiel.data.api.GeneralResponse
import com.dicoding.submissionintermediatebiel.data.api.LoginResponse
import com.dicoding.submissionintermediatebiel.data.api.UploadStoryResponse
import com.dicoding.submissionintermediatebiel.data.pref.UserModel
import com.dicoding.submissionintermediatebiel.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    suspend fun saveSession(user: UserModel) {
        Log.d("saveSession() prefSave", user.toString())
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun login(email: String, password: String): LoginResponse{
        return apiService.login(email, password)
    }

    suspend fun register(name: String, email: String, password: String): GeneralResponse{
        return apiService.register(name, email, password)
    }

    suspend fun getStories(): FetchAllStoryResponse{
        return apiService.getStories()
    }

    suspend fun uploadImage(imageFile: MultipartBody.Part, description: RequestBody): UploadStoryResponse {
        return apiService.uploadImage(imageFile, description)
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(userPreference, apiService)
            }.also { instance = it }
    }
}