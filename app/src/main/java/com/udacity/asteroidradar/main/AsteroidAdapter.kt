package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.ListItemAsteroidBinding

class AsteroidAdapter: ListAdapter<Asteroid, AsteroidAdapter.AsteroidViewHolder>(DiffCallBack) {
    companion object DiffCallBack: DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val layout = ListItemAsteroidBinding.inflate(LayoutInflater.from(parent.context))
        return AsteroidViewHolder(layout)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.bind(asteroid)
    }

    class AsteroidViewHolder(private val binding: ListItemAsteroidBinding): ViewHolder(binding.root){
        fun bind(asteroid: Asteroid){
            binding.asteroidItem = asteroid
            binding.executePendingBindings()
        }
    }


}