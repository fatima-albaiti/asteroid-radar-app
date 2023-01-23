package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.ListItemAsteroidBinding

class AsteroidAdapter(private val clickListener: AsteroidClickListener): ListAdapter<Asteroid, AsteroidAdapter.AsteroidViewHolder>(DiffCallBack) {
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
        return AsteroidViewHolder(layout, clickListener)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val asteroid = getItem(position)
        holder.bind(asteroid)
    }

    class AsteroidViewHolder(private val binding: ListItemAsteroidBinding, private val clickListener: AsteroidClickListener): ViewHolder(binding.root){
        fun bind(asteroid: Asteroid){
            binding.asteroidItem = asteroid
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    class AsteroidClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }

}