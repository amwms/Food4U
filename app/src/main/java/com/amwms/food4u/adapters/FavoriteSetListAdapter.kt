package com.amwms.food4u.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amwms.food4u.database.entities.FavoriteSet
import com.amwms.food4u.databinding.FavoriteSetItemBinding

class FavoriteSetAdapter(
    private val onItemClicked: (FavoriteSet) -> Unit
) : ListAdapter<FavoriteSet, FavoriteSetAdapter.FavoriteSetViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<FavoriteSet>() {
            override fun areItemsTheSame(oldItem: FavoriteSet, newItem: FavoriteSet): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FavoriteSet, newItem: FavoriteSet): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteSetViewHolder {
        val viewHolder = FavoriteSetViewHolder(
            FavoriteSetItemBinding.inflate(
                LayoutInflater.from( parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: FavoriteSetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FavoriteSetViewHolder(
        private var binding: FavoriteSetItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        
        fun bind(data: FavoriteSet) {
            binding.setNameTextView.text = data.setName
        }
    }
}