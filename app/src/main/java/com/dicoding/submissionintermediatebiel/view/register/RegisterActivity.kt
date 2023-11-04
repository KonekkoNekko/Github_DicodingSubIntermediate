package com.dicoding.submissionintermediatebiel.view.register

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog

import com.dicoding.submissionintermediatebiel.databinding.ActivityRegisterBinding
import com.dicoding.submissionintermediatebiel.view.ViewModelFactory
import com.dicoding.submissionintermediatebiel.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModelRegister by viewModels<RegisterViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelRegister.errorMessage.observe(this){
            showAlert(it)
        }

        binding.btnLoginAccess.setOnClickListener {
            viewModelRegister.isLoading.observe(this){
                showLoading(it)
            }

            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            viewModelRegister.registerUser(name, email, password)
        }

        animation()
    }

    private fun showAlert(it: String?) {
        val buildDialog = AlertDialog.Builder(this)
        buildDialog.setMessage(it)
            .setCancelable(false)
            .setPositiveButton("Baik") { dialog, _ ->
                dialog.dismiss()
                if (it == "User created"){
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                } else {
                    val intentStay = Intent(this, RegisterActivity::class.java)
                    startActivity(intentStay)
                }
            }
        val alert = buildDialog.create()
        alert.setTitle("Alert")
        alert.show()
    }

    private fun showLoading(it: Boolean) {
        binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
    }

    private fun animation() {
        ObjectAnimator.ofFloat(binding.ivRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }
}