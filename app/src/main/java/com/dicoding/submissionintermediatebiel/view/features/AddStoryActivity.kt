package com.dicoding.submissionintermediatebiel.view.features

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.dicoding.submissionintermediatebiel.R
import com.dicoding.submissionintermediatebiel.data.api.ApiConfig
import com.dicoding.submissionintermediatebiel.data.api.UploadStoryResponse
import com.dicoding.submissionintermediatebiel.data.local.Repository
import com.dicoding.submissionintermediatebiel.data.local.ResultState
import com.dicoding.submissionintermediatebiel.databinding.ActivityAddStoryBinding
import com.dicoding.submissionintermediatebiel.view.ViewModelFactory
import com.dicoding.submissionintermediatebiel.view.features.camera.getImageUri
import com.dicoding.submissionintermediatebiel.view.features.camera.reduceFileImage
import com.dicoding.submissionintermediatebiel.view.features.camera.uriToFile
import com.dicoding.submissionintermediatebiel.view.main.MainActivity
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUrl: Uri? = null
    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnUpload.setOnClickListener { uploadImage() }
        supportActionBar?.hide()
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUrl = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUrl = getImageUri(this)
        launcherIntentCamera.launch(currentImageUrl)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUrl?.let { uri ->
            Log.d("Image URI", "showImage: $uri")
            binding.ivImageAdd.setImageURI(uri)
        }
    }


    private fun uploadImage() {
        currentImageUrl?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val description = binding.edDescriptionAdd.text.toString()

            viewModel.isLoading.observe(this) {
                showLoading(true)
            }

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody =
                MultipartBody.Part.createFormData("photo", imageFile.name, requestImageFile)

            lifecycleScope.launch {
                try {
                    val uploadResult = viewModel.uploadFile(multipartBody, requestBody)
                    val error = uploadResult.error
                    val message = uploadResult.message
                    showLoading(false)

                    if (error == false && message != null) {
                        handleShowDialog(message)
                    }
                } catch (e: HttpException) {
                    val errorResponse = Gson().fromJson(
                        e.response()?.errorBody()?.string(),
                        UploadStoryResponse::class.java
                    )
                    val errorMessage = errorResponse.message.toString()
                    handleShowDialog(errorMessage)
                }
            }
        }
    }

    private fun handleShowDialog(message: String) {
        val buildDialog = AlertDialog.Builder(this@AddStoryActivity)
        buildDialog.apply {
            if (message != "success"){
                setTitle("Hooray!")
                setMessage(message)
                setPositiveButton("Continue"){_, _ ->
                    val intent = Intent(this@AddStoryActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                val alert = create()
                alert.show()
            } else {
                setTitle("Oh Nooooo!")
                setMessage(message)
                setPositiveButton("OK"){_, _ ->
                    val intent = Intent(this@AddStoryActivity, AddStoryActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                val alert = create()
                alert.show()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbAdd.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}