package com.dicoding.submissionintermediatebiel.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.dicoding.submissionintermediatebiel.databinding.ActivityMainBinding
import com.dicoding.submissionintermediatebiel.view.ViewModelFactory
import com.dicoding.submissionintermediatebiel.view.login.LoginActivity
import com.dicoding.submissionintermediatebiel.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this){user ->
            if (!user.isLogin){
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }

            binding.btnLogout.setOnClickListener {
                viewModel.logout()
            }
        }

        animation()
    }

    private fun animation() {
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
}