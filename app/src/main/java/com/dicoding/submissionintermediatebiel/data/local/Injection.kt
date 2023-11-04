package com.dicoding.submissionintermediatebiel.data.local

import android.content.Context
import com.dicoding.submissionintermediatebiel.data.api.ApiConfig
import com.dicoding.submissionintermediatebiel.data.pref.UserPreference
import com.dicoding.submissionintermediatebiel.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): Repository{
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(pref, apiService)
    }
}