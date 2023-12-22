package com.dicoding.picodiploma.loginwithanimation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.Response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ItemRowBinding
import com.dicoding.picodiploma.loginwithanimation.dummy.ListHistory

class StoryAdapter : ListAdapter<ListHistory, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    class StoryViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListHistory){
            binding.root.setOnClickListener {
                onItemClickCallback.onItemClicked(item)}
            binding.apply {
                Glide.with(itemView)
                    .load(item.history.image)
                    .centerCrop()
                    .into(image)
                param.text = item.history.stressLvl
            }
        }
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: ListHistory)
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        Companion.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListHistory>() {
            override fun areItemsTheSame(oldItem: ListHistory, newItem: ListHistory): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListHistory, newItem: ListHistory): Boolean {
                return oldItem == newItem
            }
        }
        private lateinit var onItemClickCallback: OnItemClickCallback
    }
}
