package com.amwms.food4u.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amwms.food4u.database.entities.Dish
import com.amwms.food4u.databinding.SetItemBinding
import java.lang.StringBuilder

class SetAdapter(
    private val onItemClicked: (Pair<Int, List<Dish>>) -> Unit
) : ListAdapter<Pair<Int, List<Dish>>, SetAdapter.SetViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Pair<Int, List<Dish>>>() {
            override fun areItemsTheSame(oldItem: Pair<Int, List<Dish>>,
                                         newItem: Pair<Int, List<Dish>>): Boolean {
                return oldItem.first == newItem.first
            }

            override fun areContentsTheSame(oldItem: Pair<Int, List<Dish>>,
                                            newItem: Pair<Int, List<Dish>>): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        val viewHolder = SetViewHolder(
            SetItemBinding.inflate(
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

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SetViewHolder(
        private var binding: SetItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Pair<Int, List<Dish>>) {
            binding.setTitle.text = "set     ${data.first} kcal"
            binding.dishesIncludedTextView.text = castDishList(data.second)
        }

        private fun castDishList(dishList: List<Dish>): String {
            val stringBuilder = StringBuilder()
            for (i in 0 until dishList.size) {
                stringBuilder.append(dishList.get(i).name)

                if (i != dishList.size - 1) { // adding comma between allergens
                    stringBuilder.append(", ")
                }
            }

            return stringBuilder.toString()
        }
    }
}