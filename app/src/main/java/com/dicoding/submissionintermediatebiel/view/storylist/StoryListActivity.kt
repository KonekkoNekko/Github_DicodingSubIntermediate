package com.dicoding.submissionintermediatebiel.view.storylist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submissionintermediatebiel.data.api.ListStoryItem
import com.dicoding.submissionintermediatebiel.databinding.ActivityStoryList2Binding
import com.dicoding.submissionintermediatebiel.view.ViewModelFactory
import com.dicoding.submissionintermediatebiel.view.features.DetailActivity

class StoryListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryList2Binding
    private lateinit var adapter: StoryListAdapter
    private val viewModel by viewModels<StoryListViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryList2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAddNewStory.setOnClickListener {
            val intent = Intent(this@StoryListActivity, null)
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvStoryList.layoutManager = layoutManager
        adapter = StoryListAdapter {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.NAME, it.name)
            intent.putExtra(DetailActivity.IMG, it.photoUrl)
            intent.putExtra(DetailActivity.DATE, it.createdAt)
            intent.putExtra(DetailActivity.DESC, it.description)
            startActivity(intent)
        }
        binding.rvStoryList.adapter = adapter
        viewModel.isLoading.observe(this){
            showLoading(it)
        }

        viewModel.listStories.observe(this){
            setStoriesList(it)
        }
    }

    private fun setStoriesList(it: List<ListStoryItem>?) {
        adapter.submitList(it)
    }

    private fun showLoading(it: Boolean) {
        binding.pbStoryList.visibility = if (it) View.VISIBLE else View.GONE
    }
}