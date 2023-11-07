package com.dicoding.submissionintermediatebiel.view.storylist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submissionintermediatebiel.R
import com.dicoding.submissionintermediatebiel.data.api.ListStoryItem
import com.dicoding.submissionintermediatebiel.databinding.ActivityStoryList2Binding
import com.dicoding.submissionintermediatebiel.view.ViewModelFactory
import com.dicoding.submissionintermediatebiel.view.features.AddStoryActivity
import com.dicoding.submissionintermediatebiel.view.features.DetailActivity
import com.dicoding.submissionintermediatebiel.view.welcome.WelcomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            val intent = Intent(this@StoryListActivity, AddStoryActivity::class.java)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(itemMenuAppbar: MenuItem): Boolean {
        when (itemMenuAppbar.itemId) {
            R.id.logout_menu -> {
                CoroutineScope(Dispatchers.IO).launch { viewModel.logout() }
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(itemMenuAppbar)
    }
    private fun setStoriesList(it: List<ListStoryItem>?) {
        adapter.submitList(it)
    }

    private fun showLoading(it: Boolean) {
        binding.pbStoryList.visibility = if (it) View.VISIBLE else View.GONE
    }
}