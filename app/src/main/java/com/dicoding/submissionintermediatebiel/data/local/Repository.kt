package com.dicoding.submissionintermediatebiel.data.local

import com.dicoding.submissionintermediatebiel.data.api.ApiService
import com.dicoding.submissionintermediatebiel.data.api.FetchAllStoryResponse
import com.dicoding.submissionintermediatebiel.data.api.GeneralResponse
import com.dicoding.submissionintermediatebiel.data.api.LoginResponse
import com.dicoding.submissionintermediatebiel.data.pref.UserModel
import com.dicoding.submissionintermediatebiel.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class Repository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    suspend fun saveSession(user: UserModel) {
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