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
        val calorieTypes = listOf(
            "energy (kcal)  ",
            "fat (g)  ",
            "saturated fat (g)  " ,
            "carbohydrates (g)  ",
            "sugar (g)  ",
            "protein (g)  ",
            "salt (g)  "
        )

        fun bind(data: Calories) {
            binding.caloriesTypeTextView.text = calorieTypes[0]
            binding.caloriesCountTextView.text = data.energy_kcal.toString()

            binding.fatTypeTextView.text = calorieTypes[1]
            binding.fatCountTextView.text = data.fat_g.toString()

            binding.saturatedFatTypeTextView.text = calorieTypes[2]
            binding.saturatedFatCountTextView.text = data.saturatedFat_g.toString()

            binding.carbohydratesTypeTextView.text = calorieTypes[3]
            binding.carbohydratesCountTextView.text = data.carbohydrates_g.toString()

            binding.sugarTypeTextView.text = calorieTypes[4]
            binding.sugarCountTextView.text = data.sugar_g.toString()

            binding.proteinTypeTextView.text = calorieTypes[5]
            binding.proteinCountTextView.text = data.protein_g.toString()

            binding.saltTypeTextView.text = calorieTypes[6]
            binding.saltCountTextView.text = data.salt_g.toString()
        }
    }
}