package com.dicoding.submissionintermediatebiel.view.login

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.dicoding.submissionintermediatebiel.data.api.GeneralResponse
import com.dicoding.submissionintermediatebiel.data.pref.UserModel
import com.dicoding.submissionintermediatebiel.databinding.ActivityLoginBinding
import com.dicoding.submissionintermediatebiel.view.ViewModelFactory
import com.dicoding.submissionintermediatebiel.view.main.MainActivity
import com.dicoding.submissionintermediatebiel.view.register.RegisterActivity
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModelLogin by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLoginAccess.setOnClickListener {
            viewModelLogin.isLoading.observe(this) {
                showLoading(it)
            }

            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            lifecycleScope.launch {
                val buildDialog = AlertDialog.Builder(this@LoginActivity)
                try {
                    val result = viewModelLogin.loginUser(email, password)
                    val message = result.message
                    if (message != "error") {
                        val token = result.loginResult?.token.toString()
                        Log.d("Token Login Act:", token)
                        val isSessionSaved = viewModelLogin.saveSession(UserModel(email, token))
                        if (isSessionSaved) {
                            buildDialog.apply {
                                setTitle("Berhasil!")
                                setMessage(result.message)
                                setPositiveButton("Baik") { _, _ ->
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                val alert = create()
                                alert.show()
                            }
                        } else {
                            buildDialog.apply {
                                setTitle("Alert")
                                setMessage("Coba Login Kembali")
                                setPositiveButton("Ok") { _, _ ->
                                    val intentStay =
                                        Intent(this@LoginActivity, LoginActivity::class.java)
                                    startActivity(intentStay)
                                }
                                val alertMessage = create()
                                alertMessage.setTitle("Alert")
                                alertMessage.show()
                            }
                        }
                    }
                } catch (e: HttpException) {
                    val jsonInString = e.response()?.errorBody()?.string()
                    val errorBody = Gson().fromJson(jsonInString, GeneralResponse::class.java)
                    val errorMessage = errorBody.message
                    buildDialog.apply {
                        setTitle("Alert")
                        setMessage(errorMessage)
                        setPositiveButton("Ok") { _, _ ->
                            val intentStay =
                                Intent(this@LoginActivity, RegisterActivity::class.java)
                            startActivity(intentStay)
                        }
                        val alertMessage = create()
                        alertMessage.setTitle("Alert")
                        alertMessage.show()
                    }
                }
            }
        }

        animation()
        supportActionBar?.hide()
    }

    private fun animation() {
        ObjectAnimator.ofFloat(binding.ivLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun showLoading(it: Boolean) {
        binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
    }
}