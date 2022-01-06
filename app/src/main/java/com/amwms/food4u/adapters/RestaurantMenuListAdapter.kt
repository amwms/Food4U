package com.amwms.food4u.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amwms.food4u.database.entities.Dish
import com.amwms.food4u.databinding.RestaurantMenuItemBinding

class RestaurantMenuAdapter(
    private val onItemClicked: (Dish) -> Unit
) : ListAdapter<Dish, RestaurantMenuAdapter.RestaurantMenuViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Dish>() {
            override fun areItemsTheSame(oldItem: Dish, newItem: Dish): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Dish, newItem: Dish): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantMenuViewHolder {
        val viewHolder = RestaurantMenuViewHolder(
            RestaurantMenuItemBinding.inflate(
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

    override fun onBindViewHolder(holder: RestaurantMenuViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RestaurantMenuViewHolder(
        private var binding: RestaurantMenuItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(dish: Dish) {
            binding.dishNameTextView.text = dish.name
        }
    }
}