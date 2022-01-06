package com.amwms.food4u.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amwms.food4u.database.entities.Calories
import com.amwms.food4u.databinding.CaloriesItemBinding

class CaloriesAdapter(
    private val onItemClicked: (Calories) -> Unit
) : ListAdapter<Calories, CaloriesAdapter.CaloriesViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Calories>() {
            override fun areItemsTheSame(oldItem: Calories, newItem: Calories): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Calories, newItem: Calories): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaloriesViewHolder {
        val viewHolder = CaloriesViewHolder(
            CaloriesItemBinding.inflate(
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

    override fun onBindViewHolder(holder: CaloriesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CaloriesViewHolder(
        private var binding: CaloriesItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        val calorieTypes = listOf("energy_kj", "energy_kcal", "fat_g", "saturated_fat_g" , "carbohydrates_g", "sugar_g", "protein_g", "salt_g")

        fun bind(data: Calories) {
            binding.caloriesTypeTextView.text = "energy (kcal)"
            binding.caloriesCountTextView.text = data.energy_kcal.toString()
        }
    }
}