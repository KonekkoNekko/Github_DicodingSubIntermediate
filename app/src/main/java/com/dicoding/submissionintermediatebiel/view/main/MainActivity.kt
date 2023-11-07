package com.dicoding.submissionintermediatebiel.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.submissionintermediatebiel.data.api.GeneralResponse
import com.dicoding.submissionintermediatebiel.databinding.ActivityMainBinding
import com.dicoding.submissionintermediatebiel.view.ViewModelFactory
import com.dicoding.submissionintermediatebiel.view.storylist.StoryListActivity
import com.dicoding.submissionintermediatebiel.view.welcome.WelcomeActivity
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe user session
        viewModel.getSession().observe(this) { user ->
            Log.d("viewModel MainAct", user.toString())
            if (!user.isLogin) {
                navigateToWelcomeActivity()
            } else {
                navigateToStoryListActivity()
            }
        }

        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                viewModel.logout()
            }
            navigateToWelcomeActivity()
        }

        // Start animation
        startAnimation()
    }

    private fun startAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val welcome = ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 1f).setDuration(250)
        val logout = ObjectAnimator.ofFloat(binding.btnLogout, View.ALPHA, 1f).setDuration(250)

        AnimatorSet().apply {
            playSequentially(welcome, logout)
            startDelay = 250
        }.start()
    }

    private fun navigateToWelcomeActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun navigateToStoryListActivity() {
        val intent = Intent(this, StoryListActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}
