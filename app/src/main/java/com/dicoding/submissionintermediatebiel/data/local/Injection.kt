package com.dicoding.submissionintermediatebiel.data.local

import android.content.Context
import android.util.Log
import com.dicoding.submissionintermediatebiel.data.api.ApiConfig
import com.dicoding.submissionintermediatebiel.data.pref.UserPreference
import com.dicoding.submissionintermediatebiel.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository{
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        Log.d("Injection", user.token)
        val apiService = ApiConfig.getApiService(user.token)
        return Repository.getInstance(pref, apiService)
    }
}