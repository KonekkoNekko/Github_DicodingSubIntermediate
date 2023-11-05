package com.dicoding.submissionintermediatebiel.view.storylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.submissionintermediatebiel.Util
import com.dicoding.submissionintermediatebiel.data.api.ListStoryItem
import com.dicoding.submissionintermediatebiel.databinding.ItemStoryBinding

class StoryListAdapter(private val onItemClick: (ListStoryItem) -> Unit) :
    ListAdapter<ListStoryItem, StoryListAdapter.ItemViewHolder>(
        DIFF_CALLBACK
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val itemResponse = getItem(position)
        holder.bind(itemResponse)
        holder.itemView.setOnClickListener {
            onItemClick(itemResponse)
        }
    }

    class ItemViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(response: ListStoryItem) {
            val iso8601Date = response.createdAt.toString()
            val formattedDate = Util.formatIso8601Date(iso8601Date)
            Glide.with(binding.root)
                .load("${response.photoUrl}").into(binding.ivImageStory)
            binding.tvName.text = response.name
            binding.tvCreateAt.text = formattedDate
            binding.tvDescription.text = response.description
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}