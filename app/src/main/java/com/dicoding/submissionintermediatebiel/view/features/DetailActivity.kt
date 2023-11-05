package com.dicoding.submissionintermediatebiel.view.features

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dicoding.submissionintermediatebiel.R
import com.dicoding.submissionintermediatebiel.Util
import com.dicoding.submissionintermediatebiel.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            intent.apply {
                val iso8601Date = getStringExtra(DATE).toString()
                val formattedDate = Util.formatIso8601Date(iso8601Date)
                Glide.with(binding.root).load(getStringExtra(IMG)).into(ivImageDetail)
                tvDateDetail.text = formattedDate
                tvNameDetail.text = getStringExtra(NAME)
                tvDescriptionDetail.text = getStringExtra(DESC)
            }
        }
    }

    companion object {
        const val NAME = "DETAIL_NAME"
        const val IMG = "DETAIL_IMG"
        const val DATE = "DETAIL_DATE"
        const val DESC = "DETAIL_DESC"
    }
}